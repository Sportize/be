package com.be.sportizebe.domain.club.service;

import com.be.sportizebe.domain.chat.service.ChatRoomService;
import com.be.sportizebe.domain.club.dto.request.ClubCreateRequest;
import com.be.sportizebe.domain.club.dto.request.ClubUpdateRequest;
import com.be.sportizebe.domain.club.dto.response.*;
import com.be.sportizebe.domain.club.entity.Club;
import com.be.sportizebe.domain.club.entity.ClubMember;
import com.be.sportizebe.domain.club.exception.ClubErrorCode;
import com.be.sportizebe.domain.club.repository.ClubMemberRepository;
import com.be.sportizebe.domain.club.repository.ClubRepository;
import com.be.sportizebe.domain.user.entity.User;
import com.be.sportizebe.global.exception.CustomException;
import com.be.sportizebe.global.s3.enums.PathName;
import com.be.sportizebe.global.s3.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ClubServiceImpl implements ClubService {

    private final ClubRepository clubRepository;
    private final ChatRoomService chatRoomService;
    private final ClubMemberRepository clubMemberRepository;
    private final S3Service s3Service;

    @Override
    @Transactional
    public ClubResponse createClub(ClubCreateRequest request, MultipartFile image, User user) {
        if (clubRepository.existsByName(request.name())) {
            throw new CustomException(ClubErrorCode.CLUB_NAME_DUPLICATED);
        }

        // 이미지가 있으면 S3에 업로드
        String clubImageUrl = null;
        if (image != null && !image.isEmpty()) {
            clubImageUrl = s3Service.uploadFile(PathName.CLUB, image);
        }

        // 동호회 엔티티 생성
        Club club = request.toEntity(user, clubImageUrl);
        clubRepository.save(club);

        // 동호회 멤버 테이블에 방장(동호회 생성자) 추가
        ClubMember leaderMember = ClubMember.builder()
                .club(club)
                .user(user)
                .role(ClubMember.ClubRole.LEADER)
                .build();
        clubMemberRepository.save(leaderMember);

        // 동호회 단체 채팅방 생성
        chatRoomService.createGroup(club);

        return ClubResponse.from(club);
    }

    @Override
    @Transactional
    public ClubResponse updateClub(Long clubId, ClubUpdateRequest request, User user) {
        Club club = clubRepository.findById(clubId)
                .orElseThrow(() -> new CustomException(ClubErrorCode.CLUB_NOT_FOUND));

        // 동호회 방장만 수정 가능하도록 검증
        if (club.getLeader().getId() != user.getId()) {
            throw new CustomException(ClubErrorCode.CLUB_UPDATE_DENIED);
        }

        if (!club.getName().equals(request.name()) && clubRepository.existsByName(request.name())) {
            throw new CustomException(ClubErrorCode.CLUB_NAME_DUPLICATED);
        }

        if (request.maxMembers() != null && request.maxMembers() < club.getMembers().size()) {
            throw new CustomException(ClubErrorCode.CLUB_MAX_MEMBERS_TOO_SMALL);
        }

        club.update(request.name(), request.introduce(), request.maxMembers(), request.clubType());

        return ClubResponse.from(club);
    }

    @Override
    @Transactional
    public ClubImageResponse updateClubImage(Long clubId, MultipartFile image, User user) {
        Club club = clubRepository.findById(clubId)
                .orElseThrow(() -> new CustomException(ClubErrorCode.CLUB_NOT_FOUND));

        // 동호회 방장만 수정 가능하도록 검증
        if (club.getLeader().getId() != user.getId()) {
            throw new CustomException(ClubErrorCode.CLUB_UPDATE_DENIED);
        }

        // 기존 이미지가 있으면 S3에서 삭제
        if (club.getClubImage() != null) {
            s3Service.deleteFile(club.getClubImage());
        }

        // 새 이미지 S3에 업로드
        String clubImageUrl = s3Service.uploadFile(PathName.CLUB, image);

        // 동호회 이미지 URL 업데이트
        club.updateClubImage(clubImageUrl);

        return ClubImageResponse.from(clubImageUrl);
    }
    @Override
    @Transactional(readOnly = true)
    public ClubDetailResponse getClub(Long clubId) {
        Club club = clubRepository.findById(clubId)
                .orElseThrow(() -> new CustomException(ClubErrorCode.CLUB_NOT_FOUND));

        int memberCount = clubMemberRepository.countByClubId(clubId);

        return ClubDetailResponse.from(club, memberCount);
    }
    @Override
    @Transactional(readOnly = true)
    public ClubScrollResponse getClubsByScroll(Long cursor, int size) {

        // +1 조회해서 다음 페이지 존재 여부 판단
        Pageable pageable = PageRequest.of(0, size + 1);

        List<Club> clubs = clubRepository.findClubsByCursor(cursor, pageable);

        boolean hasNext = clubs.size() > size;

        if (hasNext) {
            clubs = clubs.subList(0, size);
        }

        List<ClubListItemResponse> items = clubs.stream()
                .map(club -> {
                    int memberCount = clubMemberRepository.countByClubId(club.getId());
                    return ClubListItemResponse.from(club, memberCount);
                })
                .toList();

        Long nextCursor = items.isEmpty()
                ? null
                : items.get(items.size() - 1).clubId();

        return new ClubScrollResponse(items, nextCursor, hasNext);
    }
    @Override
    @Transactional(readOnly = true)
    public ClubScrollResponse getMyClubsByScroll(Long cursor, int size, User user) {

        Pageable pageable = PageRequest.of(0, size + 1);

        List<Club> clubs = clubRepository.findMyClubsByCursor(user.getId(), cursor, pageable);

        boolean hasNext = clubs.size() > size;

        if (hasNext) {
            clubs = clubs.subList(0, size);
        }

        List<ClubListItemResponse> items = clubs.stream()
                .map(club -> {
                    int memberCount = clubMemberRepository.countByClubId(club.getId());
                    return ClubListItemResponse.from(club, memberCount);
                })
                .toList();

        Long nextCursor = items.isEmpty() ? null : items.get(items.size() - 1).clubId();

        return new ClubScrollResponse(items, nextCursor, hasNext);
    }

}
