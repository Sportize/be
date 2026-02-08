package com.be.sportizebe.domain.club.entity;

import com.be.sportizebe.domain.chat.entity.ChatRoom;
import com.be.sportizebe.domain.user.entity.SportType;
import com.be.sportizebe.domain.user.entity.User;
import com.be.sportizebe.global.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "clubs")
public class Club extends BaseTimeEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, unique = true)
  private String name; // 동호회 이름 == 단테 채팅방 이름

  @Column(columnDefinition = "TEXT")
  private String introduce; // 동호회 소개글

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private SportType clubType; // 동호회 관련 종목 (동호회 생성 시 선택)

  @Column(nullable = false)
  private Integer maxMembers; // 최대 정원

  private String clubImage; // 동호회 사진 URL

  @OneToOne(mappedBy = "club", fetch = FetchType.LAZY)
  private ChatRoom chatRoom; // 동호회 채팅방

  @OneToMany(mappedBy = "club", cascade = CascadeType.ALL, orphanRemoval = true)
  @Builder.Default
  private List<ClubMember> members = new ArrayList<>();

  /**
   * 동호회장(LEADER) 조회
   * ClubMember에서 LEADER 역할을 가진 멤버를 찾아 반환
   */
  private ClubMember getLeaderMember() {
    return members.stream()
        .filter(member -> member.getRole() == ClubMember.ClubRole.LEADER)
        .findFirst()
        .orElse(null);
  }

  /**
   * 동호회장 User 조회
   */
  public User getLeader() {
    ClubMember leaderMember = getLeaderMember();
    return leaderMember != null ? leaderMember.getUser() : null;
  }

  /**
   * 특정 사용자가 동호회장인지 확인
   */
  public boolean isLeader(Long userId) {
    ClubMember leaderMember = getLeaderMember();
    return leaderMember != null && leaderMember.getUser().getId() == userId;
  }

  public void update(String name, String introduce, Integer maxMembers, SportType clubType) {
    this.name = name;
    this.introduce = introduce;
    this.maxMembers = maxMembers;
    this.clubType = clubType;
  }

  public void updateClubImage(String clubImage) {
    this.clubImage = clubImage;
  }
}
