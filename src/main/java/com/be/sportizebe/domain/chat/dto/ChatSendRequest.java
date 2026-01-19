package com.be.sportizebe.domain.chat.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class ChatSendRequest {
    @NotNull
    private Long roomId;

    @NotNull
    private Long senderUserId;

    @NotNull
    @Size(max=30)
    private String senderNickname;

    @Size(max = 2000)
    private String content;

}
