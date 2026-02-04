package com.be.sportizebe.domain.notification.repository;

import com.be.sportizebe.domain.notification.entity.Notification;
import com.be.sportizebe.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

  // 특정 사용자의 알림 목록 (최신순)
  List<Notification> findByReceiverOrderByCreatedAtDesc(User receiver);

  // 특정 사용자의 읽지 않은 알림 목록
  List<Notification> findByReceiverAndIsReadFalseOrderByCreatedAtDesc(User receiver);

  // 특정 사용자의 읽지 않은 알림 개수
  long countByReceiverAndIsReadFalse(User receiver);
}