package com.be.sportizebe.domain.club.dto.request;

import com.be.sportizebe.domain.club.entity.Club;
import com.be.sportizebe.domain.user.entity.SportType;
import com.be.sportizebe.domain.user.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record ClubCreateRequest(
  @NotBlank(message = "동호회 이름은 필수 입니다.")
  @Schema(description = "동호회 이름", example = "축구 동호회") String name,
  @Schema(description = "동호회 소개", example = "매주 토요일 축구합니다") String introduce,
  @Schema(description = "최대 정원", example = "20") Integer maxMembers) {
  // 관련 종목은 파라미터로 받음
  // TODO : S3 세팅 후 imgUrl은 multipartform으로 변경

  public Club toEntity(User user) {
    return Club.builder()
      .name(name)
      .introduce(introduce)
      .maxMembers(maxMembers)
      .leader(user)
      .build();
  }
}
