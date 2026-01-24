package com.be.sportizebe.domain.chat.websocket;

import com.be.sportizebe.domain.chat.dto.response.ChatMessageResponse;
import com.be.sportizebe.domain.chat.dto.request.ChatSendRequest;
import com.be.sportizebe.domain.chat.entity.ChatMessage;
import com.be.sportizebe.domain.chat.entity.ChatRoom;
import com.be.sportizebe.domain.chat.service.ChatMessageService;
import com.be.sportizebe.domain.chat.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ChatStompController {
    private final ChatMessageService chatMessageService;
    private final ChatRoomService chatRoomService;   // ✅ 추가
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/chat.send")
    public void send(ChatSendRequest req){

        ChatRoom room = chatRoomService.getOrThrow(req.getRoomId());
        ChatMessage saved = chatMessageService.saveChat(
                room,
                req.getSenderUserId(),
                req.getSenderNickname(),
                req.getContent()
        );
        messagingTemplate.convertAndSend(
                "/topic/chat/rooms/" + req.getRoomId(),
                ChatMessageResponse.from(saved)
        );
    }
}
