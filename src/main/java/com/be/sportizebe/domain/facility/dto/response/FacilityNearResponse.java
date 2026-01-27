// src/main/java/com/be/sportizebe/domain/facility/dto/response/FacilityNearResponse.java
package com.be.sportizebe.domain.facility.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FacilityNearResponse {

    @Schema(description = "시설 ID", example = "1")
    private Long id;

    @Schema(description = "시설명", example = "수원종합운동장")
    private String facilityName;

    @Schema(description = "소개", example = "수원시 대표 종합 스포츠 시설 (축구, 육상 등)")
    private String introduce;

    @Schema(description = "썸네일 URL", example = "https://example.com/suwon-stadium.jpg")
    private String thumbnailUrl;

    @Schema(description = "종목", example = "SOCCER")
    private String facilityType;

    @Schema(description = "거리(미터)", example = "2178")
    private int distanceM;
}