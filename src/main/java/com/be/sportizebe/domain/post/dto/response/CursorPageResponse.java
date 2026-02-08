package com.be.sportizebe.domain.post.dto.response;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "커서 기반 무한 스크롤 응답")
public record CursorPageResponse<T>(

        @Schema(description = "조회된 데이터 목록")
        List<T> items,

        @Schema(description = "다음 조회를 위한 커서 값 (없으면 null)", example = "123")
        Long nextCursor,

        @Schema(description = "다음 페이지 존재 여부", example = "true")
        boolean hasNext
) {
    public static <T> CursorPageResponse<T> of(List<T> items, Long nextCursor, boolean hasNext) {
        return new CursorPageResponse<>(items, nextCursor, hasNext);
    }
}