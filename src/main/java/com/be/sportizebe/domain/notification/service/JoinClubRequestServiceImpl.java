package com.be.sportizebe.domain.notification.service;

import com.be.sportizebe.domain.club.entity.Club;
import com.be.sportizebe.domain.club.entity.ClubMember;
import com.be.sportizebe.domain.club.exception.ClubErrorCode;
import com.be.sportizebe.domain.club.repository.ClubMemberRepository;
import com.be.sportizebe.domain.club.repository.ClubRepository;
import com.be.sportizebe.domain.notification.dto.response.JoinClubRequestResponse;
import com.be.sportizebe.domain.notification.entity.JoinClubRequest;
import com.be.sportizebe.domain.notification.exception.JoinClubRequestErrorCode;
import com.be.sportizebe.domain.notification.repository.JoinClubRequestRepository;
import com.be.sportizebe.domain.user.entity.User;
import com.be.sportizebe.domain.user.exception.UserErrorCode;
import com.be.sportizebe.domain.user.repository.UserRepository;
import com.be.sportizebe.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class JoinClubRequestServiceImpl implements JoinClubRequestService {

  private final JoinClubRequestRepository joinClubRequestRepository;
  private final ClubRepository clubRepository;
  private final ClubMemberRepository clubMemberRepository;
  private final UserRepository userRepository;
  private final NotificationServiceImpl notificationService;

  @Override
  @Transactional
  public JoinClubRequestResponse requestJoin(Long clubId, Long userId) {
    User user = findUserById(userId);
    Club club = findClubById(clubId);

    // 자신이 동호회장인 동호회에는 가입 신청 불가
    if (club.isLeader(userId)) {
      throw new CustomException(JoinClubRequestErrorCode.CANNOT_JOIN_OWN_CLUB);
    }

    // 이미 가입된 회원인지 확인
    if (clubMemberRepository.existsByClubAndUser(club, user)) {
      throw new CustomException(JoinClubRequestErrorCode.ALREADY_CLUB_MEMBER);
    }

    // 이미 가입 신청했는지 확인
    if (joinClubRequestRepository.existsByUserAndClub(user, club)) {
      throw new CustomException(JoinClubRequestErrorCode.JOIN_REQUEST_ALREADY_EXISTS);
    }

    // 정원 확인
    if (club.getMembers().size() >= club.getMaxMembers()) {
      throw new CustomException(JoinClubRequestErrorCode.CLUB_FULL);
    }

    // 가입 신청 생성 (상태는 PENDING)
    JoinClubRequest joinRequest = JoinClubRequest.builder()
        .user(user)
        .club(club)
        .build();
    joinClubRequestRepository.save(joinRequest);

    // 동호회장에게 알림 전송
    User leader = club.getLeader();
    if (leader != null) {
      notificationService.createJoinRequestNotification(joinRequest, leader);
    }

    log.info("가입 신청 완료: userId={}, clubId={}", userId, clubId);
    return JoinClubRequestResponse.from(joinRequest);
  }

  @Override
  @Transactional
  public void cancelRequest(Long requestId, Long userId) {
    JoinClubRequest request = findRequestById(requestId);

    // 본인 신청만 취소 가능
    if (request.getUser().getId() != userId) {
      throw new CustomException(ClubErrorCode.CLUB_UPDATE_DENIED);
    }

    // 대기 중인 신청만 취소 가능
    if (request.getStatus() != JoinClubRequest.JoinClubRequestStatus.PENDING) {
      throw new CustomException(JoinClubRequestErrorCode.JOIN_REQUEST_NOT_PENDING);
    }

    joinClubRequestRepository.delete(request); // DELETE FROM join_club_request WHERE id = {request.getId()} 쿼리 사용됨
    log.info("가입 신청 취소: requestId={}, userId={}", requestId, userId);
  }

  @Override
  @Transactional
  public JoinClubRequestResponse approveRequest(Long requestId, Long leaderId) {
    JoinClubRequest request = findRequestById(requestId);
    Club club = request.getClub();

    // 동호회장만 승인 가능
    if (!club.isLeader(leaderId)) {
      throw new CustomException(ClubErrorCode.CLUB_UPDATE_DENIED);
    }

    // 대기 중인 신청만 승인 가능
    if (request.getStatus() != JoinClubRequest.JoinClubRequestStatus.PENDING) {
      throw new CustomException(JoinClubRequestErrorCode.JOIN_REQUEST_NOT_PENDING);
    }

    // 정원 확인
    if (club.getMembers().size() >= club.getMaxMembers()) {
      throw new CustomException(JoinClubRequestErrorCode.CLUB_FULL);
    }

    // 신청 승인
    request.accept(); // 신청 상태 변경

    // 동호회 멤버로 추가
    ClubMember newMember = ClubMember.builder()
        .club(club)
        .user(request.getUser())
        .role(ClubMember.ClubRole.MEMBER)
        .build();
    clubMemberRepository.save(newMember);

    // 신청자에게 승인 알림 전송
    notificationService.createJoinApprovedNotification(request);

    log.info("가입 승인 완료: requestId={}, userId={}", requestId, request.getUser().getId());
    return JoinClubRequestResponse.from(request);
  }

  @Override
  @Transactional
  public JoinClubRequestResponse rejectRequest(Long requestId, Long leaderId) {
    JoinClubRequest request = findRequestById(requestId);
    Club club = request.getClub();

    // 동호회장만 거절 가능
    if (!club.isLeader(leaderId)) {
      throw new CustomException(ClubErrorCode.CLUB_UPDATE_DENIED);
    }

    // 대기 중인 신청만 거절 가능
    if (request.getStatus() != JoinClubRequest.JoinClubRequestStatus.PENDING) {
      throw new CustomException(JoinClubRequestErrorCode.JOIN_REQUEST_NOT_PENDING);
    }

    // 신청 거절
    request.reject(); // 신청 상태 변경

    // 신청자에게 거절 알림 전송
    notificationService.createJoinRejectedNotification(request);

    log.info("가입 거절 완료: requestId={}, userId={}", requestId, request.getUser().getId());
    return JoinClubRequestResponse.from(request);
  }

  @Override
  public List<JoinClubRequestResponse> getPendingRequests(Long clubId, Long leaderId) {
    Club club = findClubById(clubId);

    // 동호회장만 조회 가능
    if (!club.isLeader(leaderId)) {
      throw new CustomException(ClubErrorCode.CLUB_UPDATE_DENIED);
    }

    return joinClubRequestRepository
        .findByClubAndStatus(club, JoinClubRequest.JoinClubRequestStatus.PENDING)
        .stream()
        .map(JoinClubRequestResponse::from)
        .toList();
  }

  @Override
  public List<JoinClubRequestResponse> getMyRequests(Long userId) {
    User user = findUserById(userId);
    return joinClubRequestRepository.findByUser(user)
        .stream()
        .map(JoinClubRequestResponse::from)
        .toList();
  }

  /*
    중복 코드 제거, 일관된 예외처리, 가독성, 유지보수의 목적으로 헬퍼 메서드 사용
   */
  private User findUserById(Long userId) {
    return userRepository.findById(userId)
        .orElseThrow(() -> new CustomException(UserErrorCode.USER_NOT_FOUND));
  }

  private Club findClubById(Long clubId) {
    return clubRepository.findById(clubId)
        .orElseThrow(() -> new CustomException(ClubErrorCode.CLUB_NOT_FOUND));
  }

  private JoinClubRequest findRequestById(Long requestId) {
    return joinClubRequestRepository.findById(requestId)
        .orElseThrow(() -> new CustomException(JoinClubRequestErrorCode.JOIN_REQUEST_NOT_FOUND));
  }
}