package com.be.sportizebe.domain.user.service;

import com.be.sportizebe.domain.user.dto.request.SignUpRequest;
import com.be.sportizebe.domain.user.dto.request.UpdateProfileRequest;
import com.be.sportizebe.domain.user.dto.response.ProfileImageResponse;
import com.be.sportizebe.domain.user.dto.response.SignUpResponse;
import com.be.sportizebe.domain.user.dto.response.UpdateProfileResponse;
import com.be.sportizebe.domain.user.dto.response.UserInfoResponse;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {

  // 회원가입
  SignUpResponse signUp(SignUpRequest request);

  // 프로필 사진 업로드
  ProfileImageResponse uploadProfileImage(Long userId, MultipartFile file);

  // 프로필 수정 (닉네임, 한줄소개)
  UpdateProfileResponse updateProfile(Long userId, UpdateProfileRequest request);

  // 사용자 정보 조회
  UserInfoResponse getUserInfo(Long userId);
}
