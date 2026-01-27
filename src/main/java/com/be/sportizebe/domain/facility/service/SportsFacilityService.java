package com.be.sportizebe.domain.facility.service;

import com.be.sportizebe.domain.facility.dto.request.FacilityMarkerRequest;
import com.be.sportizebe.domain.facility.dto.request.FacilityNearRequest;
import com.be.sportizebe.domain.facility.dto.response.FacilityMarkerResponse;
import com.be.sportizebe.domain.facility.dto.response.FacilityNearResponse;
import com.be.sportizebe.domain.facility.mapper.FacilityMapper;
import com.be.sportizebe.domain.facility.repository.SportsFacilityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SportsFacilityService {

    private final SportsFacilityRepository sportsFacilityRepository;

    public List<FacilityNearResponse> getNear(FacilityNearRequest request) {
        String type = (request.getType() == null) ? null : request.getType().name();

        var list = sportsFacilityRepository.findNear(
                request.getLat(),
                request.getLng(),
                request.getRadiusM(),
                request.getLimit(),
                type
        );

        return list.stream()
                .map(FacilityMapper::toNearResponse)
                .toList();
    }
    public List<FacilityMarkerResponse> getMarkers(FacilityMarkerRequest request) {
        String type = (request.getType() == null) ? null : request.getType().name(); // ✅ enum -> String

        var list = sportsFacilityRepository.findMarkersNear(
                request.getLat(),
                request.getLng(),
                request.getRadiusM(),
                request.getLimit(),
                type
        );

        return list.stream()
                .map(FacilityMapper::toMarkerResponse)
                .toList();
    }
}
