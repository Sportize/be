package com.be.sportizebe.domain.like.repository;

import com.be.sportizebe.domain.like.entity.Like;
import com.be.sportizebe.domain.like.entity.LikeTargetType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {

    // 특정 사용자가 특정 대상에 좋아요 했는지 확인
    Optional<Like> findByUserIdAndTargetTypeAndTargetId(Long userId, LikeTargetType targetType, Long targetId);

    // 특정 사용자가 특정 대상에 좋아요 했는지 여부
    boolean existsByUserIdAndTargetTypeAndTargetId(Long userId, LikeTargetType targetType, Long targetId);

    // 특정 대상의 좋아요 수
    long countByTargetTypeAndTargetId(LikeTargetType targetType, Long targetId);

    // 특정 사용자의 특정 대상 좋아요 삭제
    void deleteByUserIdAndTargetTypeAndTargetId(Long userId, LikeTargetType targetType, Long targetId);
}