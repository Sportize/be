package com.be.sportizebe.domain.notification.controller;

import com.be.sportizebe.domain.notification.dto.response.JoinClubRequestResponse;
import com.be.sportizebe.domain.notification.service.JoinClubRequestServiceImpl;
import com.be.sportizebe.global.cache.dto.UserAuthInfo;
import com.be.sportizebe.global.response.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/clubs")
@Tag(name = "join-request", description = "동호회 가입 관련 API")
public class JoinClubRequestController {

  private final JoinClubRequestServiceImpl joinClubRequestService;

  @PostMapping("/{clubId}/join")
  @Operation(summary = "가입 신청", description = "동호회에 가입을 신청합니다. 동호회장에게 알림이 전송됩니다.")
  public ResponseEntity<BaseResponse<JoinClubRequestResponse>> requestJoin(
      @Parameter(description = "동호회 ID") @PathVariable Long clubId,
      @AuthenticationPrincipal UserAuthInfo userAuthInfo) {
    JoinClubRequestResponse response = joinClubRequestService.requestJoin(clubId, userAuthInfo.getId());
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(BaseResponse.success("가입 신청 완료", response));
  }

  @DeleteMapping("/join-requests/{requestId}")
  @Operation(summary = "가입 신청 취소", description = "본인의 가입 신청을 취소합니다.")
  public ResponseEntity<BaseResponse<Void>> cancelRequest(
      @Parameter(description = "가입 신청 ID") @PathVariable Long requestId,
      @AuthenticationPrincipal UserAuthInfo userAuthInfo) {
    joinClubRequestService.cancelRequest(requestId, userAuthInfo.getId());
    return ResponseEntity.ok(BaseResponse.success("가입 신청 취소 완료", null));
  }

  @PostMapping("/join-requests/{requestId}/approve")
  @Operation(summary = "가입 승인", description = "가입 신청을 승인합니다. 동호회장만 가능합니다.")
  public ResponseEntity<BaseResponse<JoinClubRequestResponse>> approveRequest(
      @Parameter(description = "가입 신청 ID") @PathVariable Long requestId,
      @AuthenticationPrincipal UserAuthInfo userAuthInfo) {
    JoinClubRequestResponse response = joinClubRequestService.approveRequest(requestId, userAuthInfo.getId());
    return ResponseEntity.ok(BaseResponse.success("가입 승인 완료", response));
  }

  @PostMapping("/join-requests/{requestId}/reject")
  @Operation(summary = "가입 거절", description = "가입 신청을 거절합니다. 동호회장만 가능합니다.")
  public ResponseEntity<BaseResponse<JoinClubRequestResponse>> rejectRequest(
      @Parameter(description = "가입 신청 ID") @PathVariable Long requestId,
      @AuthenticationPrincipal UserAuthInfo userAuthInfo) {
    JoinClubRequestResponse response = joinClubRequestService.rejectRequest(requestId, userAuthInfo.getId());
    return ResponseEntity.ok(BaseResponse.success("가입 거절 완료", response));
  }

  @GetMapping("/{clubId}/join-requests")
  @Operation(summary = "대기 중인 가입 신청 목록", description = "동호회의 대기 중인 가입 신청 목록을 조회합니다. 동호회장만 가능합니다.")
  public ResponseEntity<BaseResponse<List<JoinClubRequestResponse>>> getPendingRequests(
      @Parameter(description = "동호회 ID") @PathVariable Long clubId,
      @AuthenticationPrincipal UserAuthInfo userAuthInfo) {
    List<JoinClubRequestResponse> response = joinClubRequestService.getPendingRequests(clubId, userAuthInfo.getId());
    return ResponseEntity.ok(BaseResponse.success("가입 신청 목록 조회 성공", response));
  }

  @GetMapping("/my-join-requests")
  @Operation(summary = "내 가입 신청 목록", description = "내가 신청한 가입 신청 목록을 조회합니다.")
  public ResponseEntity<BaseResponse<List<JoinClubRequestResponse>>> getMyRequests(
      @AuthenticationPrincipal UserAuthInfo userAuthInfo) {
    List<JoinClubRequestResponse> response = joinClubRequestService.getMyRequests(userAuthInfo.getId());
    return ResponseEntity.ok(BaseResponse.success("내 가입 신청 목록 조회 성공", response));
  }
}