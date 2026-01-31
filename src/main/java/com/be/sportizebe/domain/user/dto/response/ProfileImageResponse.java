package com.be.sportizebe.domain.user.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "프로필 이미지 업로드 응답")
public record ProfileImageResponse(
    @Schema(description = "프로필 이미지 URL", example = "https://bucket.s3.ap-northeast-2.amazonaws.com/profile/uuid.jpg")
    String profileImageUrl
) {
    public static ProfileImageResponse from(String profileImageUrl) {
        return new ProfileImageResponse(profileImageUrl);
    }
}