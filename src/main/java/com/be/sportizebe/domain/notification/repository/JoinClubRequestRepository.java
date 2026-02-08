package com.be.sportizebe.domain.notification.repository;

import com.be.sportizebe.domain.club.entity.Club;
import com.be.sportizebe.domain.notification.entity.JoinClubRequest;
import com.be.sportizebe.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface JoinClubRequestRepository extends JpaRepository<JoinClubRequest, Long> {

  // 특정 사용자의 특정 동호회 가입 신청 조회
  Optional<JoinClubRequest> findByUserAndClub(User user, Club club);

  // 특정 사용자가 특정 동호회에 이미 신청했는지 확인
  boolean existsByUserAndClub(User user, Club club);

  // 특정 동호회의 대기 중인 가입 신청 목록
  List<JoinClubRequest> findByClubAndStatus(Club club, JoinClubRequest.JoinClubRequestStatus status);

  // 특정 사용자의 가입 신청 목록
  List<JoinClubRequest> findByUser(User user);
}