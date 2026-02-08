package com.be.sportizebe.domain.club.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(title = "ClubScrollResponse", description = "동호회 목록 무한스크롤 조회 응답")
public record ClubScrollResponse(

        @Schema(description = "동호회 목록")
        List<ClubListItemResponse> items,

        @Schema(description = "다음 조회를 위한 커서 값 (마지막 clubId)", example = "15")
        Long nextCursor,

        @Schema(description = "다음 페이지 존재 여부", example = "true")
        boolean hasNext
) {
}