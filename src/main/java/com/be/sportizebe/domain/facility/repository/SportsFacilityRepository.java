package com.be.sportizebe.domain.facility.repository;

import com.be.sportizebe.domain.facility.entity.FacilityType;
import com.be.sportizebe.domain.facility.entity.SportsFacility;
import com.be.sportizebe.domain.facility.repository.projection.FacilityMarkerProjection;
import com.be.sportizebe.domain.facility.repository.projection.FacilityNearProjection;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.util.List;

// Java에서 거리를 계산하는게 아니라, DB가 돌린 결과 "숫자"를 받아오는 거다.
public interface SportsFacilityRepository extends Repository<SportsFacility, Long> {
    // 주변 가까운 체육시설 조회 쿼리
    @Query(value = """
        SELECT
            sf.id AS id,
            sf.facility_name AS facilityName,
            sf.introduce AS introduce,
            sf.thumbnail_url AS thumbnailUrl,
            sf.facility_type AS facilityType,
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
        AND (:type IS NULL OR sf.facility_type = :type)
        ORDER BY distanceM
        LIMIT :limit
        """, nativeQuery = true)
    List<FacilityNearProjection> findNear(
            @Param("lat") double lat,
            @Param("lng") double lng,
            @Param("radiusM") int radiusM,
            @Param("limit") int limit,
            @Param("type") String type
    );
    // 마커 전용 쿼리
    @Query(value = """
        SELECT
            sf.id AS id,
            sf.facility_name AS facilityName,
            sf.facility_type AS facilityType,
            ST_Y(sf.location::geometry) AS lat,
            ST_X(sf.location::geometry) AS lng,
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
        AND (:type IS NULL OR sf.facility_type = :type)
        ORDER BY distanceM
        LIMIT :limit
        """, nativeQuery = true)
    List<FacilityMarkerProjection> findMarkersNear(
            @Param("lat") double lat,
            @Param("lng") double lng,
            @Param("radiusM") int radiusM,
            @Param("limit") int limit,
            @Param("type") String type
    );
}