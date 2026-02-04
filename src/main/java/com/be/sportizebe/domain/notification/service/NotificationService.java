package com.be.sportizebe.domain.notification.service;

import com.be.sportizebe.domain.notification.dto.response.NotificationResponse;
import com.be.sportizebe.domain.notification.entity.JoinClubRequest;
import com.be.sportizebe.domain.notification.entity.Notification;
import com.be.sportizebe.domain.user.entity.User;

import java.util.List;

public interface NotificationService {

  // 가입 신청 알림 생성 및 웹소켓 전송
  Notification createJoinRequestNotification(JoinClubRequest joinRequest, User receiver);

  // 가입 승인 알림 생성 및 웹소켓 전송
  Notification createJoinApprovedNotification(JoinClubRequest joinRequest);

  // 가입 거절 알림 생성 및 웹소켓 전송
  Notification createJoinRejectedNotification(JoinClubRequest joinRequest);

  // 사용자의 모든 알림 조회
  List<NotificationResponse> getNotifications(User user);

  // 읽지 않은 알림 조회
  List<NotificationResponse> getUnreadNotifications(User user);

  // 읽지 않은 알림 개수
  long getUnreadCount(User user);

  // 알림 읽음 처리
  void markAsRead(Long notificationId);
}