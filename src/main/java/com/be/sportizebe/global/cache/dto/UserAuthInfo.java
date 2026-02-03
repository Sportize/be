package com.be.sportizebe.global.cache.dto;

import com.be.sportizebe.domain.user.entity.Role;
import com.be.sportizebe.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * JWT 인증 필터에서 사용할 캐시용 사용자 정보 DTO
 * User 엔티티의 연관관계(posts 등)로 인한 직렬화 문제를 방지
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserAuthInfo implements Serializable { // Serializable : 직렬화 가능하다라는 마커 표시
    private Long id;
    private String username;
    private String nickname;
    private Role role;

    public static UserAuthInfo from(User user) {
        return new UserAuthInfo(
                user.getId(),
                user.getUsername(),
                user.getNickname(),
                user.getRole()
        );
    }
}