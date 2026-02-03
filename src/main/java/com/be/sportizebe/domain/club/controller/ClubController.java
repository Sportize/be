package com.be.sportizebe.domain.club.controller;

import com.be.sportizebe.domain.club.dto.request.ClubCreateRequest;
import com.be.sportizebe.domain.club.dto.request.ClubUpdateRequest;
import com.be.sportizebe.domain.club.dto.response.ClubImageResponse;
import com.be.sportizebe.domain.club.dto.response.ClubResponse;
import com.be.sportizebe.domain.club.service.ClubServiceImpl;
import com.be.sportizebe.global.response.BaseResponse;
import com.be.sportizebe.global.cache.dto.UserAuthInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/clubs")
@Tag(name = "club", description = "동호회 관련 API")
public class ClubController {

  private final ClubServiceImpl clubService;

  @PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  @Operation(summary = "동호회 생성", description = "종목별 동호회를 생성합니다. 생성한 사용자가 동호회장이 됩니다. (이미지 첨부 가능)")
  public ResponseEntity<BaseResponse<ClubResponse>> createClub(
      @RequestPart("request") @Valid ClubCreateRequest request,
      @RequestPart(value = "image", required = false) MultipartFile image,
      @AuthenticationPrincipal UserAuthInfo userAuthInfo) {
    ClubResponse response = clubService.createClub(request, image, userAuthInfo.getId());
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(BaseResponse.success("동호회 생성 성공", response));
  }

  @PutMapping("/{clubId}")
  @Operation(summary = "동호회 수정", description = "동호회 정보를 수정합니다. 동호회장만 수정할 수 있습니다.")
  public ResponseEntity<BaseResponse<ClubResponse>> updateClub(
      @PathVariable Long clubId,
      @RequestBody @Valid ClubUpdateRequest request,
      @AuthenticationPrincipal UserAuthInfo userAuthInfo) {
    ClubResponse response = clubService.updateClub(clubId, request, userAuthInfo.getId());
    return ResponseEntity.ok(BaseResponse.success("동호회 수정 성공", response));
  }

  @PostMapping(value = "/{clubId}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  @Operation(summary = "동호회 사진 수정", description = "동호회 사진을 수정합니다. 동호회장만 수정할 수 있습니다.")
  public ResponseEntity<BaseResponse<ClubImageResponse>> updateClubImage(
      @Parameter(description = "동호회 ID") @PathVariable Long clubId,
      @RequestPart("image") MultipartFile image,
      @AuthenticationPrincipal UserAuthInfo userAuthInfo) {
    ClubImageResponse response = clubService.updateClubImage(clubId, image, userAuthInfo.getId());
    return ResponseEntity.ok(BaseResponse.success("동호회 사진 수정 성공", response));
  }
}
