package com.be.sportizebe.domain.like.repository;

import com.be.sportizebe.domain.like.entity.Like;
import com.be.sportizebe.domain.like.entity.LikeTargetType;
import com.be.sportizebe.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {

    // 특정 사용자가 특정 대상에 좋아요 했는지 확인
    // npe를 방지하기 위해 Optional로 null이 올 수 있는 값을 감쌈(참조시 npe 발생x)
    Optional<Like> findByUserAndTargetTypeAndTargetId(User user, LikeTargetType targetType, Long targetId);

    // 특정 사용자가 특정 대상에 좋아요 했는지 여부
    boolean existsByUserAndTargetTypeAndTargetId(User user, LikeTargetType targetType, Long targetId);

    // 특정 대상의 좋아요 수
    long countByTargetTypeAndTargetId(LikeTargetType targetType, Long targetId);

    // 특정 사용자의 특정 대상 좋아요 삭제
    void deleteByUserAndTargetTypeAndTargetId(User user, LikeTargetType targetType, Long targetId);
}