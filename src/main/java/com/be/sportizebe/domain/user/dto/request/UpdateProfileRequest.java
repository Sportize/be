package com.be.sportizebe.domain.user.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(title = "UpdateProfileRequest DTO", description = "프로필 수정 요청")
public record UpdateProfileRequest(
    @Schema(description = "닉네임", example = "새로운닉네임")
    @NotBlank(message = "닉네임은 필수입니다")
    @Size(min = 2, max = 20, message = "닉네임은 2~20자 사이여야 합니다")
    String nickname,

    @Schema(description = "한줄 소개", example = "안녕하세요! 축구를 좋아합니다.")
    @Size(max = 100, message = "한줄 소개는 100자 이내여야 합니다")
    String introduce
) {}