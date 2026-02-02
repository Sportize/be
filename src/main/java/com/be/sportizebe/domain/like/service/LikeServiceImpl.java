package com.be.sportizebe.domain.like.service;

import com.be.sportizebe.domain.like.dto.response.LikeResponse;
import com.be.sportizebe.domain.like.entity.Like;
import com.be.sportizebe.domain.like.entity.LikeTargetType;
import com.be.sportizebe.domain.like.repository.LikeRepository;
import com.be.sportizebe.domain.user.entity.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class LikeServiceImpl implements LikeService {

  private final LikeRepository likeRepository;

  @Override
  @Transactional
  @Caching(evict = {
          @CacheEvict(cacheNames = "likeCount", key = "#targetType + ':' + #targetId"),
          @CacheEvict(cacheNames = "likeStatus", key = "#user.id + ':' + #targetType + ':' + #targetId")
  })
  public LikeResponse toggleLike(User user, LikeTargetType targetType, Long targetId) {

    boolean liked = false; // 좋아요 여부 변수

    if(likeRepository.existsByUserAndTargetTypeAndTargetId(user, targetType, targetId)) {
      likeRepository.deleteByUserAndTargetTypeAndTargetId(user, targetType, targetId);
    } else {
      // 좋아요 추가
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
  @Cacheable(cacheNames = "likeStatus", key = "#user.id + ':' + #targetType + ':' + #targetId")
  public boolean isLiked(User user, LikeTargetType targetType, Long targetId) {
    return likeRepository.existsByUserAndTargetTypeAndTargetId(user, targetType, targetId);
  }

  @Override
  @Cacheable(cacheNames = "likeCount", key = "#targetType + ':' + #targetId")
  public long getLikeCount(LikeTargetType targetType, Long targetId) { // 좋아요 개수 추적 메서드
    return likeRepository.countByTargetTypeAndTargetId(targetType, targetId);
  }
}
