package com.be.sportizebe.domain.facility.service;

import com.be.sportizebe.domain.facility.dto.request.FacilityMarkerRequest;
import com.be.sportizebe.domain.facility.dto.request.FacilityNearRequest;
import com.be.sportizebe.domain.facility.dto.response.FacilityMarkerResponse;
import com.be.sportizebe.domain.facility.dto.response.FacilityNearResponse;

import java.util.List;

public interface SportsFacilityService {

    List<FacilityNearResponse> getNear(FacilityNearRequest request); // 현재 위치 기준 반경 내 체육시설을 거리순으로 조히
    // @Param: request 위치, 반경, 종목 등의 조회 조건
    // @return: 체육시설 상세 목록

    List<FacilityMarkerResponse> getMarkers(FacilityMarkerRequest request); // 지도 중심 좌표 기준 반경 내 체육시설 마커 목록 조회
    // @Param: request 지도 중심 좌표, 반경, 종목 등의 조회 조건
    // @return: 지도 마커용 체육시설 목록
}