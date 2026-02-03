package com.be.sportizebe.domain.user.service;

import com.be.sportizebe.domain.user.dto.request.SignUpRequest;
import com.be.sportizebe.domain.user.dto.response.ProfileImageResponse;
import com.be.sportizebe.domain.user.dto.response.SignUpResponse;
import com.be.sportizebe.domain.user.entity.Gender;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {

  // 회원가입
  SignUpResponse signUp(SignUpRequest request);

  // 프로필 사진 업로드
  ProfileImageResponse uploadProfileImage(Long userId, MultipartFile file);
}
