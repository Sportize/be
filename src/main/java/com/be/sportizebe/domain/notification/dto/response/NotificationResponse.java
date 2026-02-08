package com.be.sportizebe.domain.notification.dto.response;

import com.be.sportizebe.domain.notification.entity.Notification;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
@Schema(title = "NotificationResponse DTO", description = "알림 응답")
public record NotificationResponse(
    @Schema(description = "알림 ID") Long id,
    @Schema(description = "알림 타입") Notification.NotificationType type,
    @Schema(description = "읽음 여부") Boolean isRead,
    @Schema(description = "관련 가입 신청 ID") Long joinRequestId,
    @Schema(description = "동호회 이름") String clubName,
    @Schema(description = "신청자 닉네임") String applicantNickname,
    @Schema(description = "관련 대상 ID (댓글, 채팅 등)") Long targetId,
    @Schema(description = "알림 생성 일시") LocalDateTime createdAt
) {
  public static NotificationResponse from(Notification notification) {
    var joinRequest = notification.getJoinClubRequest();

    return NotificationResponse.builder()
        .id(notification.getId())
        .type(notification.getType())
        .isRead(notification.getIsRead())
        .joinRequestId(joinRequest != null ? joinRequest.getId() : null)
        .clubName(joinRequest != null ? joinRequest.getClub().getName() : null)
        .applicantNickname(joinRequest != null ? joinRequest.getUser().getNickname() : null)
        .targetId(notification.getTargetId())
        .createdAt(notification.getCreatedAt())
        .build();
  }
}