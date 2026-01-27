// src/main/java/com/be/sportizebe/domain/facility/dto/response/FacilityMarkerResponse.java
package com.be.sportizebe.domain.facility.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FacilityMarkerResponse {

    @Schema(description = "시설 ID", example = "1")
    private Long id;

    @Schema(description = "시설명", example = "수원종합운동장")
    private String facilityName;

    @Schema(description = "종목", example = "SOCCER")
    private String facilityType;

    @Schema(description = "위도", example = "37.2869")
    private double lat;

    @Schema(description = "경도", example = "127.0095")
    private double lng;

    @Schema(description = "중심으로부터 거리(미터)", example = "1200")
    private int distanceM;
}