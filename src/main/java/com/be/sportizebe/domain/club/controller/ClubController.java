package com.be.sportizebe.domain.club.controller;

import com.be.sportizebe.domain.club.dto.request.ClubCreateRequest;
import com.be.sportizebe.domain.club.dto.request.ClubUpdateRequest;
import com.be.sportizebe.domain.club.dto.response.ClubDetailResponse;
import com.be.sportizebe.domain.club.dto.response.ClubImageResponse;
import com.be.sportizebe.domain.club.dto.response.ClubResponse;
import com.be.sportizebe.domain.club.dto.response.ClubScrollResponse;
import com.be.sportizebe.domain.club.service.ClubService;
import com.be.sportizebe.domain.user.entity.User;
import com.be.sportizebe.global.cache.dto.UserAuthInfo;
import com.be.sportizebe.global.response.BaseResponse;
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
@Tag(name = "club", description = "동호회 관리 관련 API")
public class ClubController {

    private final ClubService clubService;

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

    @GetMapping
    @Operation(summary = "모든 동호회 조회 (무한스크롤)",
            description = "커서 기반 무한스크롤 방식으로 동호회 목록을 조회합니다.")
    public ResponseEntity<BaseResponse<ClubScrollResponse>> getClubs(
            @RequestParam(required = false) Long cursor,
            @RequestParam(defaultValue = "20") int size
    ) {
        ClubScrollResponse response = clubService.getClubsByScroll(cursor, size);
        return ResponseEntity.ok(
                BaseResponse.success("동호회 목록 조회 성공", response)
        );
    }

    @GetMapping("/{clubId}")
    @Operation(summary = "동호회 상세 조회", description = "동호회 단건 상세 정보를 조회합니다.")
    public ResponseEntity<BaseResponse<ClubDetailResponse>> getClub(
            @Parameter(description = "동호회 ID") @PathVariable Long clubId
    ) {
        ClubDetailResponse response = clubService.getClub(clubId);
        return ResponseEntity.ok(BaseResponse.success("동호회 상세 조회 성공", response));
    }

    @GetMapping("/me")
    @Operation(summary = "내가 가입한 동호회 조회(무한스크롤)", description = "로그인한 사용자가 가입한 동호회를 커서 기반 무한스크롤로 조회합니다.")
    public ResponseEntity<BaseResponse<ClubScrollResponse>> getMyClubs(
            @Parameter(description = "커서(마지막 clubId). 첫 조회는 null") @RequestParam(required = false) Long cursor,
            @Parameter(description = "조회 개수", example = "10") @RequestParam(defaultValue = "10") int size,
            @AuthenticationPrincipal User user
    ) {
        ClubScrollResponse response = clubService.getMyClubsByScroll(cursor, size, user);
        return ResponseEntity.ok(BaseResponse.success("내 동호회 조회 성공", response));
    }
}
