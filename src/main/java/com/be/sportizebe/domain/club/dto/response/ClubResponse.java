package com.be.sportizebe.domain.club.dto.response;

import com.be.sportizebe.domain.club.entity.Club;
import com.be.sportizebe.domain.user.entity.SportType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(title = "ClubResponse DTO", description = "동호회 관련 응답")
public record ClubResponse(
    @Schema(description = "동호회 ID", example = "1") Long clubId,
    @Schema(description = "동호회 이름", example = "축구 동호회") String name,
    @Schema(description = "동호회 소개", example = "매주 토요일 축구합니다") String introduce,
    @Schema(description = "동호회 관련 종목", example = "SOCCER") SportType clubType,
    @Schema(description = "최대 정원", example = "20") Integer maxMembers,
    @Schema(description = "동호회장 닉네임", example = "닉네임") String leaderNickname) {

  public static ClubResponse from(Club club) {
    return ClubResponse.builder()
      .clubId(club.getId())
      .name(club.getName())
      .introduce(club.getIntroduce())
      .clubType(club.getClubType())
      .maxMembers(club.getMaxMembers())
      .leaderNickname(club.getLeader().getNickname())
      .build();
  }
}
