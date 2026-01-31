package com.be.sportizebe.domain.facility.service;

import com.be.sportizebe.domain.facility.dto.request.FacilityMarkerRequest;
import com.be.sportizebe.domain.facility.dto.request.FacilityNearRequest;
import com.be.sportizebe.domain.facility.dto.response.FacilityMarkerResponse;
import com.be.sportizebe.domain.facility.dto.response.FacilityNearResponse;
import com.be.sportizebe.domain.facility.mapper.FacilityMapper;
import com.be.sportizebe.domain.facility.repository.SportsFacilityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SportsFacilityServiceImpl implements SportsFacilityService {

    private final SportsFacilityRepository sportsFacilityRepository;

    @Override
    @Cacheable(
            cacheNames = "facilityNear",
            key = "#root.args[0].generateCacheKey()"
    )
    public List<FacilityNearResponse> getNear(FacilityNearRequest request) {
        String type = (request.getType() == null) ? null : request.getType().name();

        return sportsFacilityRepository.findNear(
                        request.getLat(),
                        request.getLng(),
                        request.getRadiusM(),
                        request.getLimit(),
                        type
                ).stream()
                .map(FacilityMapper::toNearResponse)
                .toList();
    }

    @Override
    @Cacheable(
            cacheNames = "facilityMarkers",
            key = "#root.args[0].generateCacheKey()"
    )
    public List<FacilityMarkerResponse> getMarkers(FacilityMarkerRequest request) {
        String type = (request.getType() == null) ? null : request.getType().name();

        return sportsFacilityRepository.findMarkersNear(
                        request.getLat(),
                        request.getLng(),
                        request.getRadiusM(),
                        request.getLimit(),
                        type
                ).stream()
                .map(FacilityMapper::toMarkerResponse)
                .toList();
    }
}