// src/main/java/com/be/sportizebe/domain/facility/dto/request/FacilityMarkerRequest.java
package com.be.sportizebe.domain.facility.dto.request;

import com.be.sportizebe.domain.facility.entity.FacilityType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FacilityMarkerRequest {

    @Schema(description = "지도 중심 위도", example = "37.2869", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "지도 중심 위도(centerLat)는 필수입니다")
    @DecimalMin(value = "-90.0", message = "위도는 -90.0 이상이어야 합니다")
    @DecimalMax(value = "90.0", message = "위도는 90.0 이하여야 합니다")
    private Double lat;

    @Schema(description = "지도 중심 경도", example = "127.0095", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "지도 중심 경도(centerLng)는 필수입니다")
    @DecimalMin(value = "-180.0", message = "경도는 -180.0 이상이어야 합니다")
    @DecimalMax(value = "180.0", message = "경도는 180.0 이하여야 합니다")
    private Double lng;

    @Schema(description = "반경(미터)", example = "5000", defaultValue = "5000")
    @Min(value = 100, message = "반경은 최소 100m 이상이어야 합니다")
    @Max(value = 20000, message = "반경은 최대 20km까지 가능합니다")
    private Integer radiusM = 5000;

    @Schema(description = "조회 개수 제한", example = "200", defaultValue = "200")
    @Min(value = 1, message = "limit는 최소 1 이상이어야 합니다")
    @Max(value = 500, message = "limit는 최대 500까지 가능합니다")
    private Integer limit = 200;

    @Schema(description = "종목 필터(선택)", example = "SOCCER", nullable = true)
    private FacilityType type;
}