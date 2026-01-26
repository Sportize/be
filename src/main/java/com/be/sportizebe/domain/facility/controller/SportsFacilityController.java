package com.be.sportizebe.domain.facility.controller;

import com.be.sportizebe.domain.facility.dto.FacilityNearResponse;
import com.be.sportizebe.domain.facility.service.SportsFacilityService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/facilities")
public class SportsFacilityController {

    private final SportsFacilityService sportsFacilityService;

    @GetMapping("/near")
    public List<FacilityNearResponse> near(
            @RequestParam double lat,
            @RequestParam double lng,
            @RequestParam(defaultValue = "3000") int radiusM,
            @RequestParam(defaultValue = "50") int limit,
            @RequestParam(required = false) String type

    ) {
        return sportsFacilityService.getNear(lat, lng, radiusM, limit, type);
    }
}