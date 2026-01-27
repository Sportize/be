package com.be.sportizebe.domain.facility.repository.projection;

public interface FacilityMarkerProjection {
    Long getId();
    String getFacilityName();
    String getFacilityType();
    Double getLat();
    Double getLng();
    Double getDistanceM();
}