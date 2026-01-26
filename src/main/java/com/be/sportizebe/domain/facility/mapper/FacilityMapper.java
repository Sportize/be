package com.be.sportizebe.domain.facility.mapper;

import com.be.sportizebe.domain.facility.dto.FacilityNearResponse;
import com.be.sportizebe.domain.facility.repository.FacilityNearProjection;

public interface FacilityMapper {

    public static FacilityNearResponse toNearResponse(FacilityNearProjection p){
        return FacilityNearResponse.builder()
                .id(p.getId())
                .facilityName(p.getFacilityName())
                .introduce(p.getIntroduce())
                .thumbnailUrl(p.getThumbnailUrl())
                .facilityType(p.getFacilityType())
                .distanceM((int) Math.round(p.getDistanceM()))
                .build();
    }
}
