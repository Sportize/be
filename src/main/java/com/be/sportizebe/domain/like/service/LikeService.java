package com.be.sportizebe.domain.like.service;

import com.be.sportizebe.domain.like.dto.response.LikeResponse;
import com.be.sportizebe.domain.like.entity.LikeTargetType;
import com.be.sportizebe.domain.user.entity.User;

public interface LikeService {

  // 좋아요 토글 (좋아요 추가/취소)
  LikeResponse toggleLike(User user, LikeTargetType targetType, Long targetId);

  // 좋아요 여부 확인
  boolean isLiked(User user, LikeTargetType targetType, Long targetId);

  // 좋아요 수 조회
  long getLikeCount(LikeTargetType targetType, Long targetId);
}
