package com.be.sportizebe.domain.chat.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "chat_room_members")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@IdClass(ChatRoomMemberId.class)
public class ChatRoomMember {
    public enum Role {
        OWNER,
        ADMIN,
        MEMBER
    }

    @Id
    @ManyToOne(fetch= FetchType.LAZY, optional = false)
    @JoinColumn(name = "room_id", nullable = false)
    private ChatRoom room;

    @Id
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Enumerated(EnumType.STRING)
    @Column(name="role", nullable = false, length = 10)
    private Role role;

    @Column(name="joined_at", nullable = false, updatable = false)
    private Instant joinedAt;

    @Column(name="left_at")
    private Instant leftAt;

    @PrePersist
    public void prePersist() {
        if (joinedAt == null) joinedAt = Instant.now();
        if (role == null) role = Role.MEMBER;
    }
    public boolean isActiveMember() {
        return leftAt == null;
    }

    public void leave() {
        this.leftAt = Instant.now();
    }

    public void rejoin() {
        this.leftAt = null;
        this.joinedAt = Instant.now();
    }
}
