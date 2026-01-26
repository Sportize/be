package com.be.sportizebe.domain.user.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SignUpRequest(
    @Schema(description = "사용자 아이디(이메일 형식)", example = "user@example.com")
    @NotBlank(message = "아이디를 입력해주세요.")
    @Email(message = "아이디는 이메일 형식만 지원합니다.")
    String username,

    @Schema(description = "비밀번호", example = "password123")
    @NotBlank(message = "비밀번호를 입력해주세요.")
    // @Size(min = 8, message = "비밀번호는 8자 이상이어야 합니다.")
    String password,

    @Schema(description = "닉네임", example = "스포티")
    @NotBlank(message = "사용할 닉네임을 입력해주세요.")
    String nickName
) {
}