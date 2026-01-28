package com.be.sportizebe.domain.auth.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
    @NotBlank(message = "아이디를 입력해주세요.")
    @Email(message = "아이디는 이메일 형식만 지원합니다.")
    @Schema(description = "사용자 아이디(이메일 형식)", example = "user@example.com")
    String username,

    @NotBlank(message = "비밀번호를 입력해주세요.")
    @Schema(description = "비밀번호", example = "password123")
    String password
) {
}