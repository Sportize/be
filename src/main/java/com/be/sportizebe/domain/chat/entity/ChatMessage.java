package com.be.sportizebe.domain.chat.entity;


import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

/**
 * 단체 채팅 메시지 엔티티
 * - STOMP로 들어온 메시지를 저장(옵션)하거나, 브로드캐스트할 때 사용
 */
@Entity
@Table(name = "chat_messages",
        indexes = {
                @Index(name = "idx_chat_message_room_created", columnList = "room_id, id")
        })
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ChatMessage {

    public enum Type {
        CHAT,      // 일반 채팅
        JOIN,      // 입장
        LEAVE,     // 퇴장
        SYSTEM     // 시스템 알림
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "room_id", nullable = false)
    private ChatRoom room;

    /**
     * 메시지를 보낸 사용자 ID (user 도메인과 직접 연관관계를 맺지 않고, ID만 저장하는 방식)
     * - 마이크로서비스/도메인 분리 시에도 안정적
     */
    @Column(name = "sender_user_id", nullable = false)
    private Long senderUserId;

    /**
     * 메시지를 보낸 당시의 닉네임 스냅샷
     * - 닉네임 변경/탈퇴가 있어도 메시지 표시가 깨지지 않음
     */
    @Column(name = "sender_nickname", nullable = false, length = 30)
    private String senderNickname;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, length = 10)
    private Type type;

    @Column(name = "content", length = 2000)
    private String content;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @PrePersist
    void prePersist() {
        if (createdAt == null) createdAt = Instant.now();
        if (type == null) type = Type.CHAT;
    }

    // ===== 편의 생성 메서드 =====

    public static ChatMessage chat(ChatRoom room, Long senderUserId, String senderNickname, String content) {
        return ChatMessage.builder()
                .room(room)
                .senderUserId(senderUserId)
                .senderNickname(senderNickname)
                .type(Type.CHAT)
                .content(content)
                .build();
    }

    public static ChatMessage system(ChatRoom room, String content) {
        return ChatMessage.builder()
                .room(room)
                .senderUserId(0L)
                .senderNickname("SYSTEM")
                .type(Type.SYSTEM)
                .content(content)
                .build();
    }
}
