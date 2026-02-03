package com.be.sportizebe.domain.user.service;

import com.be.sportizebe.domain.user.dto.request.SignUpRequest;
import com.be.sportizebe.domain.user.dto.request.UpdateProfileRequest;
import com.be.sportizebe.domain.user.dto.response.ProfileImageResponse;
import com.be.sportizebe.domain.user.dto.response.SignUpResponse;
import com.be.sportizebe.domain.user.dto.response.UpdateProfileResponse;
import com.be.sportizebe.domain.user.entity.Role;
import com.be.sportizebe.domain.user.entity.User;
import com.be.sportizebe.domain.user.exception.UserErrorCode;
import com.be.sportizebe.domain.user.repository.UserRepository;
import com.be.sportizebe.global.cache.service.UserCacheService;
import com.be.sportizebe.global.exception.CustomException;
import com.be.sportizebe.global.s3.enums.PathName;
import com.be.sportizebe.global.s3.service.S3Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserCacheService userCacheService;
    private final S3Service s3Service;

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
          .phoneNumber(request.phoneNumber())
          .gender(request.gender())
          .role(Role.USER)
          .build();

        User savedUser = userRepository.save(user);
        log.info("새로운 사용자 생성: {}", savedUser.getUsername());

        return SignUpResponse.from(savedUser);
    }

    @Override
    @Transactional
    public ProfileImageResponse uploadProfileImage(Long userId, MultipartFile file) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new CustomException(UserErrorCode.USER_NOT_FOUND));

        // 기존 프로필 이미지가 있으면 S3에서 삭제
        if (user.getProfileImage() != null) {
            s3Service.deleteFile(user.getProfileImage());
        }

        // 새 프로필 이미지 S3에 업로드
        String profileImageUrl = s3Service.uploadFile(PathName.PROFILE, file);

        // 사용자 프로필 이미지 URL 업데이트
        user.updateProfileImage(profileImageUrl);

        log.info("사용자 프로필 이미지 업로드 완료: userId={}, url={}", userId, profileImageUrl);

        return ProfileImageResponse.from(profileImageUrl);
    }

    @Override
    @Transactional
    public UpdateProfileResponse updateProfile(Long userId, UpdateProfileRequest request) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new CustomException(UserErrorCode.USER_NOT_FOUND));

        // 닉네임 중복 체크 (자신의 닉네임이 아닌 경우만)
        if (!user.getNickname().equals(request.nickname())
            && userRepository.existsByNickname(request.nickname())) {
            throw new CustomException(UserErrorCode.DUPLICATE_NICKNAME);
        }

        user.updateProfile(request.nickname(), request.introduce());

        // 닉네임이 UserAuthInfo에 포함되어 있으므로 캐시 무효화
        userCacheService.evictUserAuthInfo(userId);

        log.info("사용자 프로필 수정 완료: userId={}", userId);

        return UpdateProfileResponse.from(user);
    }
}