package com.be.sportizebe.domain.club.dto.response;

import com.be.sportizebe.domain.club.entity.Club;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(title = "ClubListItemResponse", description = "동호회 목록(무한스크롤) 카드 단위 응답")
public record ClubListItemResponse(

        @Schema(description = "동호회 ID", example = "1")
        Long clubId,

        @Schema(description = "동호회 이름", example = "수원 FC 풋살 동호회")
        String name,

        @Schema(description = "동호회 종목", example = "SOCCER")
        String sportType,

        @Schema(description = "동호회 소개글 (목록용 요약)", example = "주 2회 풋살을 즐기는 동호회입니다.")
        String description,

        @Schema(description = "동호회 대표 이미지 URL", example = "https://bucket.s3.ap-northeast-2.amazonaws.com/club/uuid.jpg")
        String imageUrl,

        @Schema(description = "현재 동호회 인원 수", example = "12")
        int memberCount,

        @Schema(description = "동호회 생성 일시", example = "2026-02-01T12:30:00")
        LocalDateTime createdAt
) {
    public static ClubListItemResponse from(Club club, int memberCount) {
        return new ClubListItemResponse(
                club.getId(),
                club.getName(),
                club.getClubType().name(),
                club.getIntroduce(),
                club.getClubImage(),
                memberCount,
                club.getCreatedAt()
        );
    }
}