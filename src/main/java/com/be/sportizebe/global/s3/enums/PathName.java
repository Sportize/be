package com.be.sportizebe.global.s3.enums;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PathName {
  @Schema(description = "프로필 사진")
  PROFILE("profile"),
  @Schema(description = "동호회 사진")
  CLUB("club");

  private final String folder;
}
