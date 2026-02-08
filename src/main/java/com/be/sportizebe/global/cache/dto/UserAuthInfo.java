package com.be.sportizebe.global.cache.dto;

import com.be.sportizebe.domain.user.entity.Gender;
import com.be.sportizebe.domain.user.entity.Role;
import com.be.sportizebe.domain.user.entity.SportType;
import com.be.sportizebe.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * JWT 인증 필터에서 사용할 캐시용 사용자 정보 DTO
 * User 엔티티의 연관관계(posts 등)로 인한 직렬화 문제를 방지
 * 민감정보(password, refreshToken) 제외
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserAuthInfo implements Serializable {
    private Long id;
    private String username;
    private String nickname;
    private Role role;
    private String profileImage;
    private String introduce;
    private Gender gender;
    private String phoneNumber;
    private List<SportType> interestType;

    public static UserAuthInfo from(User user) {
        return UserAuthInfo.builder()
                .id(user.getId())
                .username(user.getUsername())
                .nickname(user.getNickname())
                .role(user.getRole())
                .profileImage(user.getProfileImage())
                .introduce(user.getIntroduce())
                .gender(user.getGender())
                .phoneNumber(user.getPhoneNumber())
                .interestType(user.getInterestType())
                .build();
    }
}