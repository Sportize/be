package com.be.sportizebe.domain.like.service;

import com.be.sportizebe.domain.like.dto.response.LikeResponse;
import com.be.sportizebe.domain.like.entity.Like;
import com.be.sportizebe.domain.like.entity.LikeTargetType;
import com.be.sportizebe.domain.like.repository.LikeRepository;
import com.be.sportizebe.domain.user.entity.User;
import com.be.sportizebe.domain.user.exception.UserErrorCode;
import com.be.sportizebe.domain.user.repository.UserRepository;
import com.be.sportizebe.global.exception.CustomException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class LikeServiceImpl implements LikeService {

  private final LikeRepository likeRepository;
  private final UserRepository userRepository;

  @Override
  @Transactional
  @Caching(evict = {
          @CacheEvict(cacheNames = "likeCount", key = "#targetType + ':' + #targetId"),
          @CacheEvict(cacheNames = "likeStatus", key = "#userId + ':' + #targetType + ':' + #targetId")
  })
  public LikeResponse toggleLike(Long userId, LikeTargetType targetType, Long targetId) {

    boolean liked = false; // 좋아요 여부 변수

    if(likeRepository.existsByUserIdAndTargetTypeAndTargetId(userId, targetType, targetId)) {
      likeRepository.deleteByUserIdAndTargetTypeAndTargetId(userId, targetType, targetId);
    } else {
      // 좋아요 추가
      User user = userRepository.findById(userId)
          .orElseThrow(() -> new CustomException(UserErrorCode.USER_NOT_FOUND));
      Like like = Like.builder()
        .user(user)
        .targetType(targetType)
        .targetId(targetId)
        .build();
      likeRepository.save(like);
      liked = true;
    }
      // 토글 응답은 최신 값이 필요하므로 레포로 직접 count
      long likeCount = likeRepository.countByTargetTypeAndTargetId(targetType, targetId);

    return LikeResponse.of(liked, targetType, targetId, likeCount);
  }

  @Override
  @Cacheable(cacheNames = "likeStatus", key = "#userId + ':' + #targetType + ':' + #targetId")
  public boolean isLiked(Long userId, LikeTargetType targetType, Long targetId) {
    return likeRepository.existsByUserIdAndTargetTypeAndTargetId(userId, targetType, targetId);
  }

  @Override
  @Cacheable(cacheNames = "likeCount", key = "#targetType + ':' + #targetId")
  public long getLikeCount(LikeTargetType targetType, Long targetId) { // 좋아요 개수 추적 메서드
    return likeRepository.countByTargetTypeAndTargetId(targetType, targetId);
  }
}
