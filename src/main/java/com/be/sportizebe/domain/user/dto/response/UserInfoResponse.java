package com.be.sportizebe.domain.user.dto.response;

import com.be.sportizebe.domain.user.entity.Gender;
import com.be.sportizebe.domain.user.entity.SportType;
import com.be.sportizebe.domain.user.entity.User;
import com.be.sportizebe.global.cache.dto.UserAuthInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.util.List;

@Builder
@Schema(title = "UserInfoResponse DTO", description = "사용자 정보 조회 응답")
public record UserInfoResponse(
    @Schema(description = "사용자 ID", example = "1")
    Long userId,

    @Schema(description = "이메일(아이디)", example = "test@example.com")
    String username,

    @Schema(description = "닉네임", example = "축구왕")
    String nickname,

    @Schema(description = "한줄 소개", example = "안녕하세요!")
    String introduce,

    @Schema(description = "프로필 이미지 URL")
    String profileImage,

    @Schema(description = "성별", example = "MALE")
    Gender gender,

    @Schema(description = "전화번호", example = "010-1234-5678")
    String phoneNumber,

    @Schema(description = "관심 종목")
    List<SportType> interestType
) {
    public static UserInfoResponse from(User user) { // DB 조회
        return UserInfoResponse.builder()
            .userId(user.getId())
            .username(user.getUsername())
            .nickname(user.getNickname())
            .introduce(user.getIntroduce())
            .profileImage(user.getProfileImage())
            .gender(user.getGender())
            .phoneNumber(user.getPhoneNumber())
            .interestType(user.getInterestType())
            .build();
    }

    public static UserInfoResponse from(UserAuthInfo userAuthInfo) { // 캐시메모리 조회
        return UserInfoResponse.builder()
            .userId(userAuthInfo.getId())
            .username(userAuthInfo.getUsername())
            .nickname(userAuthInfo.getNickname())
            .introduce(userAuthInfo.getIntroduce())
            .profileImage(userAuthInfo.getProfileImage())
            .gender(userAuthInfo.getGender())
            .phoneNumber(userAuthInfo.getPhoneNumber())
            .interestType(userAuthInfo.getInterestType())
            .build();
    }
}