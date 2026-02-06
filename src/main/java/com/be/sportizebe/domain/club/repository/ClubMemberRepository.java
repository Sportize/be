package com.be.sportizebe.domain.club.repository;

import com.be.sportizebe.domain.club.entity.ClubMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClubMemberRepository extends JpaRepository<ClubMember, Long> {
    int countByClubId(Long clubId);
}
