package com.be.sportizebe.domain.facility.service;

import com.be.sportizebe.domain.facility.dto.FacilityNearResponse;
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

    public List<FacilityNearResponse> getNear(double lat, double lng, int radiusM, int limit) {
        if (radiusM <= 0) radiusM = 1000;
        if (radiusM > 20000) radiusM = 20000; // 너무 큰 반경 제한
        if (limit <= 0) limit = 50;
        if (limit > 200) limit = 200;

        List<FacilityNearProjection> rows =
                sportsFacilityRepository.findNear(lat, lng, radiusM, limit);

        return rows.stream()
                .map(r -> FacilityNearResponse.builder()
                        .id(r.getId())
                        .facilityName(r.getFacilityName())
                        .introduce(r.getIntroduce())
                        .thumbnailUrl(r.getThumbnailUrl())
                        .distanceM((int)Math.round(r.getDistanceM()))
                        .build()
                )
                .toList();
    }
}
