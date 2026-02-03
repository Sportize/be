package com.be.sportizebe.global.cache.service;

import com.be.sportizebe.global.cache.dto.UserAuthInfo;
import com.be.sportizebe.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * JWT 인증 필터에서 사용할 사용자 정보 캐시 서비스
 * Filter는 Spring AOP 프록시 범위 밖이므로 별도 서비스로 분리
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserCacheService {

    private final UserRepository userRepository;

    /**
     * userId로 사용자 인증 정보 조회 (캐시 적용)
     * 캐시 TTL: 5분 (RedisCacheConfig에서 설정)
     */
    @Cacheable(cacheNames = "userAuthInfo", key = "#userId", unless = "#result == null")
    public UserAuthInfo findUserAuthInfoById(Long userId) {
        log.debug("DB에서 사용자 정보 조회: userId={}", userId);
        return userRepository.findById(userId)
                .map(UserAuthInfo::from)
                .orElse(null);
    }

    /**
     * 사용자 정보 변경 시 캐시 무효화
     * 비밀번호 변경, 역할 변경, 회원 탈퇴 등에서 호출
     */
    @CacheEvict(cacheNames = "userAuthInfo", key = "#userId")
    public void evictUserAuthInfo(Long userId) {
        log.debug("사용자 캐시 무효화: userId={}", userId);
    }
}