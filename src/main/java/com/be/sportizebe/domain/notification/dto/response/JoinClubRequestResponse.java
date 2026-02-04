package com.be.sportizebe.domain.notification.dto.response;

import com.be.sportizebe.domain.notification.entity.JoinClubRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
@Schema(title = "JoinClubRequestResponse DTO", description = "가입 신청 응답")
public record JoinClubRequestResponse(
    @Schema(description = "가입 신청 ID") Long id,
    @Schema(description = "신청자 ID") Long userId,
    @Schema(description = "신청자 닉네임") String userNickname,
    @Schema(description = "신청자 프로필 이미지") String userProfileImage,
    @Schema(description = "동호회 ID") Long clubId,
    @Schema(description = "동호회 이름") String clubName,
    @Schema(description = "신청 상태") JoinClubRequest.JoinClubRequestStatus status,
    @Schema(description = "신청 일시") LocalDateTime createdAt
) {
  public static JoinClubRequestResponse from(JoinClubRequest request) {
    return JoinClubRequestResponse.builder()
        .id(request.getId())
        .userId(request.getUser().getId())
        .userNickname(request.getUser().getNickname())
        .userProfileImage(request.getUser().getProfileImage())
        .clubId(request.getClub().getId())
        .clubName(request.getClub().getName())
        .status(request.getStatus())
        .createdAt(request.getCreatedAt())
        .build();
  }
}