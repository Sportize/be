package com.be.sportizebe.domain.facility.mapper;

import com.be.sportizebe.domain.facility.dto.response.FacilityMarkerResponse;
import com.be.sportizebe.domain.facility.dto.response.FacilityNearResponse;
import com.be.sportizebe.domain.facility.repository.projection.FacilityMarkerProjection;
import com.be.sportizebe.domain.facility.repository.projection.FacilityNearProjection;

public interface FacilityMapper {

    static FacilityNearResponse toNearResponse(FacilityNearProjection p){
        return FacilityNearResponse.builder()
                .id(p.getId())
                .facilityName(p.getFacilityName())
                .introduce(p.getIntroduce())
                .thumbnailUrl(p.getThumbnailUrl())
                .facilityType(p.getFacilityType())
                .distanceM((int) Math.round(p.getDistanceM()))
                .build();
    }

    static FacilityMarkerResponse toMarkerResponse(FacilityMarkerProjection p){
        return FacilityMarkerResponse.builder()
                .id(p.getId())
                .facilityName(p.getFacilityName())
                .facilityType(p.getFacilityType())
                .lat(p.getLat())
                .lng(p.getLng())
                .build();
    }
}
