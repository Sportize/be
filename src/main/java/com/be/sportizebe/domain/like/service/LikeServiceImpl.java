package com.be.sportizebe.domain.like.service;

import com.be.sportizebe.domain.like.dto.response.LikeResponse;
import com.be.sportizebe.domain.like.entity.Like;
import com.be.sportizebe.domain.like.entity.LikeTargetType;
import com.be.sportizebe.domain.like.repository.LikeRepository;
import com.be.sportizebe.domain.user.entity.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class LikeServiceImpl implements LikeService {

  private final LikeRepository likeRepository;

  @Override
  @Transactional
  public LikeResponse toggleLike(User user, LikeTargetType targetType, Long targetId) {
//    Optional<Like> existingLike =
//        likeRepository.findByUserAndTargetTypeAndTargetId(user, targetType, targetId);

    boolean liked; // 좋아요 여부 변수

    try {
      // 좋아요 추가
      Like like = Like.builder()
          .user(user)
          .targetType(targetType)
          .targetId(targetId)
          .build();
      likeRepository.save(like);
      liked = true;
    } catch (DataIntegrityViolationException e) {
      // 이미 존재 -> 토글 OFF
      likeRepository.deleteByUserAndTargetTypeAndTargetId(user, targetType, targetId); // 취소 토글 시 DB에서 삭제
      liked = false;
    }

//    if (existingLike.isPresent()) { // isPresent(): 값이 존재하는지 확인하는 Optional클래스 메서드 (값 존재: true / 존재x : false)
//      // 좋아요 취소
//      likeRepository.delete(existingLike.get());
//      liked = false;
//    } else {
//
//    }

    long likeCount = getLikeCount(targetType, targetId); // 해당 타겟(게시물 or 댓글)의 좋아요 개수 저장 변수

    return LikeResponse.of(liked, targetType, targetId, likeCount);
  }

  @Override
  public boolean isLiked(User user, LikeTargetType targetType, Long targetId) {
    return likeRepository.existsByUserAndTargetTypeAndTargetId(user, targetType, targetId);
  }

  @Override
  public long getLikeCount(LikeTargetType targetType, Long targetId) { // 좋아요 개수 추적 메서드
    return likeRepository.countByTargetTypeAndTargetId(targetType, targetId);
  }
}
