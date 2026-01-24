package com.be.sportizebe.domain.chat.controller;


import com.be.sportizebe.domain.chat.dto.response.ChatMessageResponse;
import com.be.sportizebe.domain.chat.dto.response.CursorPageResponse;
import com.be.sportizebe.domain.chat.service.ChatMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chat/rooms")
// ChatMessage C: 과거 메시지 조회 (초기/스크롤) HTTP 방식
// ChatStomp C: 실시간 메시지 송수신 WebSocket(STOMP 방식)
public class ChatMessageController {
    private final ChatMessageService chatMessageService;

    /**
     * 최신/과거 메시지 커서 조회
     * - 최초: /api/chat/rooms/{roomId}/messages?size=30
     * - 더보기: /api/chat/rooms/{roomId}/messages?beforeId=12345&size=30
     * **/
    @GetMapping("/{roomId}/messages")
    public CursorPageResponse<ChatMessageResponse> list(
            @PathVariable Long roomId,
            @RequestParam(required = false) Long beforeId,
            @RequestParam(defaultValue = "30") int size
    ){
        return chatMessageService.list(roomId, beforeId, size);
    }
}
