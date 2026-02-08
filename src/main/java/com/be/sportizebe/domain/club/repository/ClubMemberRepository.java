package com.be.sportizebe.domain.club.repository;

import com.be.sportizebe.domain.club.entity.Club;
import com.be.sportizebe.domain.club.entity.ClubMember;
import com.be.sportizebe.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClubMemberRepository extends JpaRepository<ClubMember, Long> {

  // 특정 사용자가 특정 동호회에 이미 가입했는지 확인
  boolean existsByClubAndUser(Club club, User user);
    int countByClubId(Long clubId);
}
