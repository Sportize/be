package com.be.sportizebe.domain.user.controller;

import com.be.sportizebe.domain.user.dto.request.SignUpRequest;
import com.be.sportizebe.domain.user.dto.response.ProfileImageResponse;
import com.be.sportizebe.domain.user.dto.response.SignUpResponse;
import com.be.sportizebe.domain.user.entity.User;
import com.be.sportizebe.domain.user.service.UserServiceImpl;
import com.be.sportizebe.global.response.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    @PostMapping(value = "/profile-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "프로필 사진 업로드", description = "사용자 프로필 사진을 업로드합니다. (최대 5MB, jpg/jpeg/png/gif/webp 지원)")
    public ResponseEntity<BaseResponse<ProfileImageResponse>> uploadProfileImage(
        @AuthenticationPrincipal User user,
        @RequestPart("file") MultipartFile file
    ) {
        ProfileImageResponse response = userService.uploadProfileImage(user.getId(), file);
        return ResponseEntity.ok(BaseResponse.success("프로필 사진 업로드 성공", response));
    }
}