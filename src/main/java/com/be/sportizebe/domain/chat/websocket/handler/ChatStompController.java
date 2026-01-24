package com.be.sportizebe.domain.chat.websocket;

import com.be.sportizebe.domain.chat.dto.request.ChatPresenceRequest;
import com.be.sportizebe.domain.chat.dto.response.ChatMessageResponse;
import com.be.sportizebe.domain.chat.dto.request.ChatSendRequest;
import com.be.sportizebe.domain.chat.entity.ChatMessage;
import com.be.sportizebe.domain.chat.entity.ChatRoom;
import com.be.sportizebe.domain.chat.service.ChatMessageService;
import com.be.sportizebe.domain.chat.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ChatStompController {
    private final ChatMessageService chatMessageService;
    private final ChatRoomService chatRoomService;   // ✅ 추가
    private final SimpMessagingTemplate messagingTemplate;
    private final ChatSessionRegistry registry;

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

    @MessageMapping("/chat.join")
    public void join(ChatPresenceRequest req, SimpMessageHeaderAccessor headerAccessor) {
        String sessionId = headerAccessor.getSessionId();

        ChatRoom room = chatRoomService.getOrThrow(req.getRoomId());

        // 1) 세션에 “이 탭이 어느 방에 있는지” 저장
        registry.put(sessionId, req.getRoomId(), req.getUserId(), req.getNickname());

        // 2) 시스템 메시지 생성 + (옵션) DB 저장
        String content = req.getNickname() + " 님이 입장했습니다.";
        ChatMessage msg = chatMessageService.saveJoinLeave(
                room,
                ChatMessage.Type.JOIN,
                req.getUserId(),
                req.getNickname(),
                content
        );
        // 3) 브로드캐스트
        messagingTemplate.convertAndSend("/sub/chat/rooms/" + req.getRoomId(), ChatMessageResponse.from(msg));
    }
    @MessageMapping("/chat.leave")
    public void leave(ChatPresenceRequest req, SimpMessageHeaderAccessor headerAccessor) {
        String sessionId = headerAccessor.getSessionId();

        ChatRoom room = chatRoomService.getOrThrow(req.getRoomId());
        registry.remove(sessionId); // 명시적으로 나가면 제거

        String content = req.getNickname() + " 님이 퇴장했습니다.";
        ChatMessage msg = chatMessageService.saveJoinLeave(
                room,
                ChatMessage.Type.LEAVE,
                req.getUserId(),
                req.getNickname(),
                content
        );
        messagingTemplate.convertAndSend("/sub/chat/rooms/" + req.getRoomId(), ChatMessageResponse.from(msg));
    }
}
