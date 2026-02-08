package com.be.sportizebe.domain.notification.service;

import com.be.sportizebe.domain.notification.dto.response.NotificationResponse;
import com.be.sportizebe.domain.notification.entity.JoinClubRequest;
import com.be.sportizebe.domain.notification.entity.Notification;
import com.be.sportizebe.domain.notification.repository.NotificationRepository;
import com.be.sportizebe.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NotificationServiceImpl implements NotificationService {

  private final NotificationRepository notificationRepository;
  private final SimpMessagingTemplate messagingTemplate;

  @Override
  @Transactional
  public Notification createJoinRequestNotification(JoinClubRequest joinRequest, User receiver) {
    Notification notification = Notification.builder()
        .receiver(receiver)
        .type(Notification.NotificationType.JOIN_REQUEST)
        .joinClubRequest(joinRequest)
        .build();

    notificationRepository.save(notification);

    // 웹소켓으로 실시간 알림 전송
    sendNotificationToUser(receiver.getId(), notification);

    return notification;
  }

  @Override
  @Transactional
  public Notification createJoinApprovedNotification(JoinClubRequest joinRequest) {
    Notification notification = Notification.builder()
        .receiver(joinRequest.getUser())
        .type(Notification.NotificationType.JOIN_APPROVED)
        .joinClubRequest(joinRequest)
        .build();

    notificationRepository.save(notification);
    sendNotificationToUser(joinRequest.getUser().getId(), notification);

    return notification;
  }

  @Override
  @Transactional
  public Notification createJoinRejectedNotification(JoinClubRequest joinRequest) {

    Notification notification = Notification.builder()
        .receiver(joinRequest.getUser())
        .type(Notification.NotificationType.JOIN_REJECTED)
        .joinClubRequest(joinRequest)
        .build();

    notificationRepository.save(notification);
    sendNotificationToUser(joinRequest.getUser().getId(), notification);

    return notification;
  }

  @Override
  public List<NotificationResponse> getNotifications(User user) {
    return notificationRepository.findByReceiverOrderByCreatedAtDesc(user)
        .stream()
        .map(NotificationResponse::from)
        .toList();
  }

  @Override
  public List<NotificationResponse> getUnreadNotifications(User user) {
    return notificationRepository.findByReceiverAndIsReadFalseOrderByCreatedAtDesc(user)
        .stream()
        .map(NotificationResponse::from)
        .toList();
  }

  @Override
  public long getUnreadCount(User user) {
    return notificationRepository.countByReceiverAndIsReadFalse(user);
  }

  @Override
  @Transactional
  public void markAsRead(Long notificationId, Long userId) {
    Notification notification = notificationRepository.findById(notificationId)
        .orElseThrow(() -> new IllegalArgumentException("알림을 찾을 수 없습니다."));

    if (!userId.equals(notification.getReceiver().getId())) {
      throw new IllegalArgumentException("본인의 알림만 읽음 처리할 수 있습니다.");
    }

    notification.markAsRead();
  }

  /**
   * 웹소켓으로 알림 전송
   */
  private void sendNotificationToUser(Long userId, Notification notification) {
    String destination = "/sub/notifications/" + userId;
    NotificationResponse response = NotificationResponse.from(notification);
    messagingTemplate.convertAndSend(destination, response);
    log.info("알림 전송 완료: userId={}, type={}", userId, notification.getType());
  }
}