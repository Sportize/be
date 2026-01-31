package com.be.sportizebe.domain.club.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "동호회 이미지 응답")
public record ClubImageResponse(
    @Schema(description = "동호회 이미지 URL", example = "https://bucket.s3.ap-northeast-2.amazonaws.com/club/uuid.jpg")
    String clubImageUrl
) {
    public static ClubImageResponse from(String clubImageUrl) {
        return new ClubImageResponse(clubImageUrl);
    }
}