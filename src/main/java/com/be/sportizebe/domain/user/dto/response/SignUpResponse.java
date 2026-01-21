package com.be.sportizebe.domain.user.dto.response;

import com.be.sportizebe.domain.user.entity.Role;
import com.be.sportizebe.domain.user.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;

public record SignUpResponse(
    @Schema(description = "사용자 식별자", example = "1")
    Long userId,

    @Schema(description = "사용자 아이디(이메일 형식)", example = "user@example.com")
    String username,

    @Schema(description = "사용자 권한", example = "USER")
    Role role
) {
    public static SignUpResponse from(User user) {
        return new SignUpResponse(user.getId(), user.getUsername(), user.getRole());
    }
}