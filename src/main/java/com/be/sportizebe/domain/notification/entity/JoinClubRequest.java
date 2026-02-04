package com.be.sportizebe.domain.notification.entity;

import com.be.sportizebe.domain.club.entity.Club;
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
@Table(name = "join_club_request", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"user_id", "club_id"})
})
public class JoinClubRequest extends BaseTimeEntity {

  public enum JoinClubRequestStatus {
    PENDING,   // 대기
    ACCEPTED,  // 승인
    REJECTED   // 거절
  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private User user; // 가입 신청자

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "club_id", nullable = false)
  private Club club; // 가입 신청 대상 동호회

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  @Builder.Default
  private JoinClubRequestStatus status = JoinClubRequestStatus.PENDING;

  // 가입 승인 시 상태 변경
  public void accept() {
    this.status = JoinClubRequestStatus.ACCEPTED;
  }

  // 가입 거절 시 상태 변경
  public void reject() {
    this.status = JoinClubRequestStatus.REJECTED;
  }
}
