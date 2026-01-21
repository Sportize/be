package com.be.sportizebe.domain.auth.service;

import com.be.sportizebe.domain.auth.dto.request.LoginRequest;
import com.be.sportizebe.domain.auth.dto.response.LoginResponse;
import com.be.sportizebe.domain.auth.exception.AuthErrorCode;
import com.be.sportizebe.domain.user.entity.User;
import com.be.sportizebe.domain.user.exception.UserErrorCode;
import com.be.sportizebe.domain.user.repository.UserRepository;
import com.be.sportizebe.global.exception.CustomException;
import com.be.sportizebe.global.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthService {

    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByUsername(request.username())
            .orElseThrow(() -> new CustomException(UserErrorCode.USER_NOT_FOUND));

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new CustomException(AuthErrorCode.INVALID_PASSWORD);
        }

        String accessToken = jwtProvider.createAccessToken(user.getId());
        String refreshToken = jwtProvider.createRefreshToken(user.getId());

        user.updateRefreshToken(refreshToken);

        log.info("로그인 성공: {}", user.getUsername());

        return LoginResponse.of(accessToken, refreshToken, user.getId(), user.getUsername(), user.getRole());
    }
}