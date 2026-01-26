package com.be.sportizebe.domain.club.controller;

import com.be.sportizebe.domain.club.dto.request.ClubCreateRequest;
import com.be.sportizebe.domain.club.dto.response.ClubResponse;
import com.be.sportizebe.domain.club.service.ClubServiceImpl;
import com.be.sportizebe.domain.user.entity.SportType;
import com.be.sportizebe.domain.user.entity.User;
import com.be.sportizebe.global.response.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/clubs")
@Tag(name = "club", description = "동호회 관련 API")
public class ClubController {

  private final ClubServiceImpl clubService;

  @PostMapping("/{sportType}")
  @Operation(summary = "동호회 생성", description = "종목별 동호회를 생성합니다. 생성한 사용자가 동호회장이 됩니다.")
  public ResponseEntity<BaseResponse<ClubResponse>> createClub(
      @Parameter(description = "종목 (SOCCER, BASKETBALL)") @PathVariable SportType sportType,
      @RequestBody @Valid ClubCreateRequest request,
      @AuthenticationPrincipal User user) {
    ClubResponse response = clubService.createClub(sportType, request, user);
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(BaseResponse.success("동호회 생성 성공", response));
  }
}
