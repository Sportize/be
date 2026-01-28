package com.be.sportizebe.domain.club.service;

import com.be.sportizebe.domain.chat.service.ChatRoomService;
import com.be.sportizebe.domain.club.dto.request.ClubCreateRequest;
import com.be.sportizebe.domain.club.dto.response.ClubResponse;
import com.be.sportizebe.domain.club.entity.Club;
import com.be.sportizebe.domain.club.entity.ClubMember;
import com.be.sportizebe.domain.club.exception.ClubErrorCode;
import com.be.sportizebe.domain.club.repository.ClubMemberRepository;
import com.be.sportizebe.domain.club.repository.ClubRepository;
import com.be.sportizebe.domain.user.entity.SportType;
import com.be.sportizebe.domain.user.entity.User;
import com.be.sportizebe.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ClubServiceImpl implements ClubService {

  private final ClubRepository clubRepository;
  private final ChatRoomService chatRoomService;
  private final ClubMemberRepository clubMemberRepository;

  @Override
  @Transactional
  public ClubResponse createClub(ClubCreateRequest request, User user) {
    if (clubRepository.existsByName(request.name())) {
      throw new CustomException(ClubErrorCode.CLUB_NAME_DUPLICATED);
    }

    // 동호회 엔티티 생성
    Club club = request.toEntity(user);
    clubRepository.save(club);

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
}
