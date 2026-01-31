package com.be.sportizebe.domain.user.service;

import com.be.sportizebe.domain.user.dto.request.SignUpRequest;
import com.be.sportizebe.domain.user.dto.response.SignUpResponse;
import com.be.sportizebe.domain.user.entity.Role;
import com.be.sportizebe.domain.user.entity.User;
import com.be.sportizebe.domain.user.exception.UserErrorCode;
import com.be.sportizebe.domain.user.repository.UserRepository;
import com.be.sportizebe.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public SignUpResponse signUp(SignUpRequest request) {

      // 사용자 아이디 중복 체크
        if (userRepository.existsByUsername(request.username())) {
            throw new CustomException(UserErrorCode.DUPLICATE_USERNAME);
        }

        // 사용자 닉네임 중복 체크
      if (userRepository.existsByNickname(request.nickName())) {
        throw new CustomException(UserErrorCode.DUPLICATE_NICKNAME);
      }

        String encodedPassword = passwordEncoder.encode(request.password());

        User user = User.builder()
          .username(request.username())
          .password(encodedPassword)
          .nickname(request.nickName())
          .role(Role.USER)
          .build();

        User savedUser = userRepository.save(user);
        log.info("새로운 사용자 생성: {}", savedUser.getUsername());

        return SignUpResponse.from(savedUser);
    }
}