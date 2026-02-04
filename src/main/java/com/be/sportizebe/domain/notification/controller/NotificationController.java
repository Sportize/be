package com.be.sportizebe.domain.notification.controller;

import com.be.sportizebe.domain.notification.dto.response.NotificationResponse;
import com.be.sportizebe.domain.notification.service.NotificationServiceImpl;
import com.be.sportizebe.domain.user.entity.User;
import com.be.sportizebe.domain.user.exception.UserErrorCode;
import com.be.sportizebe.domain.user.repository.UserRepository;
import com.be.sportizebe.global.cache.dto.UserAuthInfo;
import com.be.sportizebe.global.exception.CustomException;
import com.be.sportizebe.global.response.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notifications")
@Tag(name = "notification", description = "알림 API")
public class NotificationController {

  private final NotificationServiceImpl notificationService;
  private final UserRepository userRepository;

  @GetMapping
  @Operation(summary = "알림 목록 조회", description = "나의 모든 알림을 조회합니다.")
  public ResponseEntity<BaseResponse<List<NotificationResponse>>> getNotifications(
      @AuthenticationPrincipal UserAuthInfo userAuthInfo) {
    User user = findUserById(userAuthInfo.getId());
    List<NotificationResponse> response = notificationService.getNotifications(user);
    return ResponseEntity.ok(BaseResponse.success("알림 목록 조회 성공", response));
  }

  @GetMapping("/unread")
  @Operation(summary = "읽지 않은 알림 조회", description = "읽지 않은 알림 목록을 조회합니다.")
  public ResponseEntity<BaseResponse<List<NotificationResponse>>> getUnreadNotifications(
      @AuthenticationPrincipal UserAuthInfo userAuthInfo) {
    User user = findUserById(userAuthInfo.getId());
    List<NotificationResponse> response = notificationService.getUnreadNotifications(user);
    return ResponseEntity.ok(BaseResponse.success("읽지 않은 알림 조회 성공", response));
  }

  @GetMapping("/unread/count")
  @Operation(summary = "읽지 않은 알림 개수", description = "읽지 않은 알림 개수를 조회합니다.")
  public ResponseEntity<BaseResponse<Long>> getUnreadCount(
      @AuthenticationPrincipal UserAuthInfo userAuthInfo) {
    User user = findUserById(userAuthInfo.getId());
    long count = notificationService.getUnreadCount(user);
    return ResponseEntity.ok(BaseResponse.success("읽지 않은 알림 개수 조회 성공", count));
  }

  @PatchMapping("/{notificationId}/read")
  @Operation(summary = "알림 읽음 처리", description = "알림을 읽음 처리합니다.")
  public ResponseEntity<BaseResponse<Void>> markAsRead(
      @Parameter(description = "알림 ID") @PathVariable Long notificationId,
      @AuthenticationPrincipal UserAuthInfo userAuthInfo) {
    notificationService.markAsRead(notificationId, userAuthInfo.getId());
    return ResponseEntity.ok(BaseResponse.success("알림 읽음 처리 완료", null));
  }

  private User findUserById(Long userId) {
    return userRepository.findById(userId)
        .orElseThrow(() -> new CustomException(UserErrorCode.USER_NOT_FOUND));
  }
}