package com.be.sportizebe.domain.club.repository;

import com.be.sportizebe.domain.club.entity.Club;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClubRepository extends JpaRepository<Club, Long> {
  boolean existsByName(String name);
}
