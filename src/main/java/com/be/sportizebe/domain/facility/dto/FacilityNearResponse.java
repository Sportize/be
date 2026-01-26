package com.be.sportizebe.domain.facility.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FacilityNearResponse {
    private Long id;
    private String facilityName;
    private String introduce;
    private String thumbnailUrl;
    private int distanceM; // 프론트에서 보기 쉽게 미터 int로
}
