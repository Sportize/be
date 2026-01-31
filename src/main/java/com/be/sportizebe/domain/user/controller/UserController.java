package com.be.sportizebe.domain.user.controller;

import com.be.sportizebe.domain.user.dto.request.SignUpRequest;
import com.be.sportizebe.domain.user.dto.response.SignUpResponse;
import com.be.sportizebe.domain.user.service.UserServiceImpl;
import com.be.sportizebe.global.response.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
@Tag(name = "user", description = "사용자 관련 API")
public class UserController {

    private final UserServiceImpl userService;

    @PostMapping("/signup")
    @Operation(summary = "회원가입", description = "이메일과 비밀번호로 회원가입")
    public ResponseEntity<BaseResponse<SignUpResponse>> signUp(@RequestBody @Valid SignUpRequest request) {
        SignUpResponse response = userService.signUp(request);
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(BaseResponse.success("회원가입 성공", response));
    }
}