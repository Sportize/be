package com.be.sportizebe.domain.facility.repository;

import com.be.sportizebe.domain.facility.entity.SportsFacility;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SportsFacilityRepository extends JpaRepository<SportsFacility, Long> {
    @Query(value = """
        SELECT
            sf.id AS id,
            sf.facility_name AS facilityName,
            sf.introduce AS introduce,
            sf.thumbnail_url AS thumbnailUrl,
            ST_Distance(
                sf.location,
                ST_SetSRID(ST_MakePoint(:lng, :lat), 4326)::geography
            ) AS distanceM
        FROM sports_facilities sf
        WHERE ST_DWithin(
            sf.location,
            ST_SetSRID(ST_MakePoint(:lng, :lat), 4326)::geography,
            :radiusM
        )
        ORDER BY distanceM ASC
        LIMIT :limit
        """, nativeQuery = true)
    List<FacilityNearProjection> findNear(
            @Param("lat") double lat,
            @Param("lng") double lng,
            @Param("radiusM") int radiusM,
            @Param("limit") int limit
    );
}
