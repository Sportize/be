package com.be.sportizebe.domain.chat.dto;

import com.be.sportizebe.domain.chat.entity.ChatRoom;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ChatRoomResponse {
    private Long roomId;
    private String name;
    private boolean active;

    public static ChatRoomResponse from(ChatRoom r) {
        return ChatRoomResponse.builder()
                .roomId(r.getId())
                .name(r.getName())
                .active(r.isActive())
                .build();
    }
}
