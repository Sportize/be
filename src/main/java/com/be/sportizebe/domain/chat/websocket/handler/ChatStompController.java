package com.be.sportizebe.domain.chat.websocket.handler;

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

    /**
     * 클라이언트가 SEND로 보낸 메세지를 받는 주소가 여기
     * @param req -> payload
     */
    @MessageMapping("/chat.send") // STOMP -> SEND (@MessageMapping : SEND frame)
    public void send(ChatSendRequest req){

        ChatRoom room = chatRoomService.getOrThrow(req.getRoomId());
        ChatMessage saved = chatMessageService.saveChat(
                room,
                req.getSenderUserId(),
                req.getSenderNickname(),
                req.getContent()
        );

        /**
         * 채팅방을 구독 중인 모든 클라이언트에게 메시지를 뿌리는 로직
         * messagingTemplate : 서버 → 클라이언트 메시지 전송용 객체, STOMP 브로커에게 메세지를 던지는 역할
         * 객체 → 메시지로 바꿔서 → 목적지로 보낸다
         */
        messagingTemplate.convertAndSend(
            "/sub/chat/rooms/" + req.getRoomId(), // 채팅방 단위 브로드캐스트
            ChatMessageResponse.from(saved)
        );
    }
}
