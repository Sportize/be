package com.be.sportizebe.domain.chat.dto;

import com.be.sportizebe.domain.chat.entity.ChatMessage;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;

@Getter
@Builder
public class ChatMessageResponse {
    private Long messageId;
    private Long roomId;
    private ChatMessage.Type type;

    private Long senderUserId;
    private String senderNickname;

    private String content;
    private Instant createdAt;
    // Instant 쓰는 이유:
    // 서버 내부 (클라이언트에 노출 안되는 부분)은 절대적인 시간을 나타내기 위해서 Instant 사용하고
    // 클라이언트에게 표시되는 건 LocalDateTime을 통해서 직관적인 ex. 2000-01-17 12:32 이렇게 표현

    public static ChatMessageResponse from(ChatMessage m){
        return ChatMessageResponse.builder()
                .messageId(m.getId())
                .roomId(m.getRoom().getId())
                .type(m.getType())
                .senderUserId(m.getSenderUserId())
                .senderNickname(m.getSenderNickname())
                .content(m.getContent())
                .createdAt(m.getCreatedAt())
                .build();
    }
}
