package com.be.sportizebe.domain.facility.service;

import com.be.sportizebe.domain.facility.dto.FacilityNearResponse;
import com.be.sportizebe.domain.facility.mapper.FacilityMapper;
import com.be.sportizebe.domain.facility.repository.FacilityNearProjection;
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

    public List<FacilityNearResponse> getNear(
            double lat,
            double lng,
            long radiusM,
            int limit,
            String type
    ) {
        if (radiusM <= 0) radiusM = 1000;
        if (radiusM > 20000) radiusM = 20000;
        if (limit <= 0) limit = 50;
        if (limit > 200) limit = 200;

        List<FacilityNearProjection> rows =
                sportsFacilityRepository.findNear(lat, lng, radiusM, limit, type);

        return rows.stream()
                .map(FacilityMapper::toNearResponse)
                .toList();
    }
}
