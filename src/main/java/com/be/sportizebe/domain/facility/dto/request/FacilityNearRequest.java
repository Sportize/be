// src/main/java/com/be/sportizebe/domain/facility/dto/request/FacilityNearRequest.java
package com.be.sportizebe.domain.facility.dto.request;

import com.be.sportizebe.domain.facility.dto.CacheKeyProvider;
import com.be.sportizebe.domain.facility.entity.FacilityType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FacilityNearRequest implements CacheKeyProvider {

    @Schema(description = "위도", example = "37.2662", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "위도(lat)는 필수입니다")
    @DecimalMin(value = "-90.0", message = "위도는 -90.0 이상이어야 합니다")
    @DecimalMax(value = "90.0", message = "위도는 90.0 이하여야 합니다")
    private Double lat;

    @Schema(description = "경도", example = "127.0006", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "경도(lng)는 필수입니다")
    @DecimalMin(value = "-180.0", message = "경도는 -180.0 이상이어야 합니다")
    @DecimalMax(value = "180.0", message = "경도는 180.0 이하여야 합니다")
    private Double lng;

    @Schema(description = "반경(미터)", example = "3000", defaultValue = "3000")
    @Min(value = 100, message = "반경은 최소 100m 이상이어야 합니다")
    @Max(value = 10000, message = "반경은 최대 10km까지 가능합니다")
    private Integer radiusM = 3000;

    @Schema(description = "조회 개수 제한", example = "50", defaultValue = "50")
    @Min(value = 1, message = "limit는 최소 1 이상이어야 합니다")
    @Max(value = 100, message = "limit는 최대 100까지 가능합니다")
    private Integer limit = 50;

    @Schema(description = "종목 필터(선택)", example = "BASKETBALL", nullable = true)
    private FacilityType type;

    @Override
    public String generateCacheKey() {
        // 소수점 4자리 반올림 (약 11m 격자)
        double gridLat = Math.round(this.lat * 10000) / 10000.0;
        double gridLng = Math.round(this.lng * 10000) / 10000.0;
        String typeName = (type == null) ? "ALL" : type.name();

        return String.format("%.4f:%.4f:%d:%d:%s",
                gridLat, gridLng, radiusM, limit, typeName);
    }
}