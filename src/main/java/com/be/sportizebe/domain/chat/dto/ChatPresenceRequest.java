package com.be.sportizebe.domain.chat.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ChatPresenceRequest {
    private Long roomId;
    private Long userId;
    private String nickname;
}