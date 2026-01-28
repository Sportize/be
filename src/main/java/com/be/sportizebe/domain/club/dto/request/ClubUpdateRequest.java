package com.be.sportizebe.domain.club.dto.request;

import com.be.sportizebe.domain.user.entity.SportType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record ClubUpdateRequest(
    @NotBlank(message = "동호회 이름은 필수 입니다.")
    @Schema(description = "동호회 이름", example = "축구 동호회") String name,
    @Schema(description = "동호회 소개", example = "매주 토요일 축구합니다") String introduce,
    @Schema(description = "동호회 관련 종목", example = "SOCCER") SportType clubType,
    @Schema(description = "최대 정원", example = "20") Integer maxMembers) {
}
