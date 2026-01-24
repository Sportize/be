package com.be.sportizebe.domain.chat.entity;


import com.be.sportizebe.domain.post.entity.Post;
import com.be.sportizebe.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "chat_rooms")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ChatRoom {

    public enum ChatRoomType {
        NOTE,
        GROUP
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="name", nullable = false, length = 100)
    private String name;

    @Column(nullable = false)
    private Integer maxMembers; // 채팅방 최대 정원

    private ChatRoomType chatRoomType; // 채팅방 타입 (단체, 1:1)

    @Column(name="is_active", nullable = false)
    private boolean active;

    @Column(name="created_at", nullable = false, updatable = false)
    private Instant createdAt;

    // 1대1 채팅(쪽지)용 필드
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post; // 연관 게시글

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "host_user_id")
    private User hostUser; // 게시글 작성자

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "guest_user_id")
    private User guestUser; // 채팅 요청자

    @PrePersist
    void prePersist() {
        if (createdAt == null) createdAt = Instant.now();
        // 기본 활성화
        active = true;
    }

    public void deactivate() {
        this.active = false;
    }
}