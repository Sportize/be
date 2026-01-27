package com.be.sportizebe.domain.facility.repository.projection;

public interface FacilityNearProjection {
    // 쿼리 결과를 "필요한 컬럼 + 계산컬럼"만 깔끔하게 받기 위한 인터페이스
    // 인터페이스로 받아버리면
    Long getId();
    String getFacilityName();
    String getIntroduce();
    String getFacilityType();
    String getThumbnailUrl();
    Double getDistanceM();
}
