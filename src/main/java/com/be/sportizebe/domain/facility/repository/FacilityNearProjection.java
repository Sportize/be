package com.be.sportizebe.domain.facility.repository;

public interface FacilityNearProjection {
    Long getId();
    String getFacilityName();
    String getIntroduce();
    String getFacilityType();
    String getThumbnailUrl();
    Double getDistanceM();
}
