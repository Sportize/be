package com.be.sportizebe.domain.club.dto.request;

import com.be.sportizebe.domain.club.entity.Club;
import com.be.sportizebe.domain.user.entity.SportType;
import com.be.sportizebe.domain.user.entity.User;
import jakarta.validation.constraints.NotBlank;

public record ClubCreateRequest(
  @NotBlank(message = "동호회 이름은 필수 입니다.")
  String name,
  String introduce,
  Integer maxMembers) {
  // 관련 종목은 파라미터로 받음
  // TODO : S3 세팅 후 imgUrl은 multipartform으로 변경

  public Club toEntity(SportType sportType, User user) {
    return Club.builder()
      .name(name)
      .introduce(introduce)
      .maxMembers(maxMembers)
      .sportType(sportType)
      .leader(user)
      .build();
  }
}
