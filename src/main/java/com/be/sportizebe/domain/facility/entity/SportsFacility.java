package com.be.sportizebe.domain.facility.entity;

import jakarta.persistence.*;
import lombok.*;
import org.locationtech.jts.geom.Point;

@Entity
@Table(name = "sports_facilities")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class SportsFacility {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String facilityName;

    @Column(columnDefinition = "text")
    private String introduce;

    // 리소스 부담 생각해서 일단 썸네일 이미지 1장으로만 구현하는 방식
    private String thumbnailUrl;

    @Column(columnDefinition = "geography(Point, 4326)", nullable = false)
    private Point location;

    // 변경 메서드(Dirty Checking용)
    public void changeInfo(String facilityName, String introduce, String thumbnailUrl) {
        if (facilityName != null) this.facilityName = facilityName;
        if (introduce != null) this.introduce = introduce;
        if (thumbnailUrl != null) this.thumbnailUrl = thumbnailUrl;
    }

    public void changeLocation(Point location) {
        this.location = location;
    }
}
