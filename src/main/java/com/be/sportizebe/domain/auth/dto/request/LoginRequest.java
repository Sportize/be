package com.be.sportizebe.domain.auth.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
    @NotBlank(message = "아이디를 입력해주세요.")
    @Email(message = "아이디는 이메일 형식만 지원합니다.")
    String username,

    @NotBlank(message = "비밀번호를 입력해주세요.")
    String password
) {
}