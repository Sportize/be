package com.be.sportizebe.domain.notification.entity;

import com.be.sportizebe.domain.user.entity.User;
import com.be.sportizebe.global.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "notifications", indexes = {
    @Index(name = "idx_notification_receiver", columnList = "receiver_id"),
    @Index(name = "idx_notification_is_read", columnList = "receiver_id, is_read")
})
public class Notification extends BaseTimeEntity {

  // TODO : 알람 종류 구체화해서 수정
  public enum NotificationType {
    JOIN_REQUEST,   // 가입 신청 (동호회장에게)
    JOIN_APPROVED,  // 가입 승인 (신청자에게)
    JOIN_REJECTED,  // 가입 거절 (신청자에게)
    CHAT,           // 새 채팅 메시지
    COMMENT         // 새 댓글
  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "receiver_id", nullable = false)
  private User receiver; // 알림 수신자

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private NotificationType type;

  @Column(nullable = false)
  @Builder.Default
  private Boolean isRead = false;

  // 가입 신청 관련 알림용 (JOIN_REQUEST, JOIN_APPROVED, JOIN_REJECTED)
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "join_request_id")
  private JoinClubRequest joinClubRequest;

  // 댓글 알림용 - targetId와 targetType으로 다형성 처리
  // COMMENT: postId, CHAT: chatRoomId 등
  private Long targetId;

  public void markAsRead() {
    this.isRead = true;
  }
}
