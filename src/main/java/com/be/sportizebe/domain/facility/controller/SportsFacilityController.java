// src/main/java/com/be/sportizebe/domain/facility/controller/SportsFacilityController.java
package com.be.sportizebe.domain.facility.controller;

import com.be.sportizebe.domain.facility.dto.request.FacilityMarkerRequest;
import com.be.sportizebe.domain.facility.dto.request.FacilityNearRequest;
import com.be.sportizebe.domain.facility.dto.response.FacilityMarkerResponse;
import com.be.sportizebe.domain.facility.dto.response.FacilityNearResponse;
import com.be.sportizebe.domain.facility.service.SportsFacilityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@Tag(name = "Sports Facility", description = "체육시설 조회 API")
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/facilities")
public class SportsFacilityController {

    private final SportsFacilityService sportsFacilityService;

    @Operation(summary = "주변 체육시설 상세 목록", description = "현재 위치(lat/lng) 기준 반경 내 체육시설을 거리순으로 조회합니다.")
    @GetMapping("/near")
    public List<FacilityNearResponse> near(
            @ParameterObject @Valid @ModelAttribute FacilityNearRequest request
    ) {
        return sportsFacilityService.getNear(request);
    }

    @Operation(summary = "지도 마커 목록", description = "지도 중심 좌표(centerLat/centerLng) 기준 반경 내 체육시설 마커 정보를 조회합니다.")
    @GetMapping("/markers")
    public List<FacilityMarkerResponse> markers(
            @ParameterObject @Valid @ModelAttribute FacilityMarkerRequest request
    ) {
        return sportsFacilityService.getMarkers(request);
    }
}