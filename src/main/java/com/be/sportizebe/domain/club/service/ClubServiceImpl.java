package com.be.sportizebe.domain.club.service;

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
  private final ClubMemberRepository clubMemberRepository;

  @Override
  @Transactional
  public ClubResponse createClub(SportType sportType, ClubCreateRequest request, User user) {
    if (clubRepository.existsByName(request.name())) {
      throw new CustomException(ClubErrorCode.CLUB_NAME_DUPLICATED);
    }

    Club club = request.toEntity(sportType, user);
    clubRepository.save(club);

    ClubMember leaderMember = ClubMember.builder()
        .club(club)
        .user(user)
        .role(ClubMember.ClubRole.LEADER)
        .build();
    clubMemberRepository.save(leaderMember);

    return ClubResponse.from(club);
  }
}
