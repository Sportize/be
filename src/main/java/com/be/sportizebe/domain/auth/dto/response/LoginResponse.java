package com.be.sportizebe.domain.auth.dto.response;

import com.be.sportizebe.domain.user.entity.Role;
import io.swagger.v3.oas.annotations.media.Schema;

public record LoginResponse(
    @Schema(description = "액세스 토큰", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    String accessToken,

    @Schema(description = "리프레시 토큰", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    String refreshToken,

    @Schema(description = "사용자 식별자", example = "1")
    Long userId,

    @Schema(description = "사용자 아이디(이메일 형식)", example = "imjuyongp@gmail.com")
    String username,

    @Schema(description = "사용자 권한", example = "USER")
    Role role
) {
    public static LoginResponse of(String accessToken, String refreshToken, Long userId, String username, Role role) {
        return new LoginResponse(accessToken, refreshToken, userId, username, role);
    }
}