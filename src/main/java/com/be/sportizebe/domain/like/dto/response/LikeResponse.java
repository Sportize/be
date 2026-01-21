package com.be.sportizebe.domain.like.dto.response;

import com.be.sportizebe.domain.like.entity.LikeTargetType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(title = "LikeResponse DTO", description = "좋아요 응답")
public record LikeResponse(
    @Schema(description = "좋아요 여부", example = "true") boolean liked,
    @Schema(description = "대상 타입", example = "POST") LikeTargetType targetType,
    @Schema(description = "대상 ID", example = "1") Long targetId,
    @Schema(description = "총 좋아요 수", example = "10") long likeCount) {

  public static LikeResponse of(boolean liked, LikeTargetType targetType, Long targetId, long likeCount) {
    return LikeResponse.builder()
        .liked(liked)
        .targetType(targetType)
        .targetId(targetId)
        .likeCount(likeCount)
        .build();
  }
}
