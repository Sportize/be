package com.be.sportizebe.domain.user.dto.response;

import com.be.sportizebe.domain.user.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(title = "UpdateProfileResponse DTO", description = "프로필 수정 응답")
public record UpdateProfileResponse(
    @Schema(description = "사용자 ID", example = "1")
    Long userId,

    @Schema(description = "닉네임", example = "새로운닉네임")
    String nickname,

    @Schema(description = "한줄 소개", example = "안녕하세요! 축구를 좋아합니다.")
    String introduce
) {
    public static UpdateProfileResponse from(User user) {
        return UpdateProfileResponse.builder()
            .userId(user.getId())
            .nickname(user.getNickname())
            .introduce(user.getIntroduce())
            .build();
    }
}