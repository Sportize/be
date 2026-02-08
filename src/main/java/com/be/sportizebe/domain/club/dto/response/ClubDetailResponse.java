package com.be.sportizebe.domain.club.dto.response;

import com.be.sportizebe.domain.club.entity.Club;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(title = "ClubDetailResponse", description = "동호회 상세 조회 응답")
public record ClubDetailResponse(

        @Schema(description = "동호회 ID", example = "1")
        Long clubId,

        @Schema(description = "동호회 이름", example = "수원 FC 풋살 동호회")
        String name,

        @Schema(description = "동호회 소개글 (전체)", example = "매주 화, 토 저녁에 풋살을 즐기는 동호회입니다.")
        String introduce,

        @Schema(description = "동호회 종목", example = "SOCCER")
        String clubType,

        @Schema(description = "최대 정원", example = "20")
        int maxMembers,

        @Schema(description = "현재 동호회 인원 수", example = "12")
        int currentMembers,

        @Schema(description = "동호회 대표 이미지 URL", example = "https://bucket.s3.ap-northeast-2.amazonaws.com/club/uuid.jpg")
        String clubImageUrl,

        @Schema(description = "동호회 생성 일시", example = "2026-02-01T12:30:00")
        LocalDateTime createdAt
) {
    public static ClubDetailResponse from(Club club, int memberCount) {
        return new ClubDetailResponse(
                club.getId(),
                club.getName(),
                club.getIntroduce(),
                club.getClubType().name(),
                club.getMaxMembers(),
                memberCount,
                club.getClubImage(),
                club.getCreatedAt()
        );
    }
}