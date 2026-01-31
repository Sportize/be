package com.be.sportizebe.domain.user.service;

import com.be.sportizebe.domain.user.dto.request.SignUpRequest;
import com.be.sportizebe.domain.user.dto.response.SignUpResponse;

public interface UserService {

  SignUpResponse signUp(SignUpRequest request); // 회원가입
}
