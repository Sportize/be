package com.be.sportizebe.domain.chat.service;

import com.be.sportizebe.domain.chat.dto.ChatMessageResponse;
import com.be.sportizebe.domain.chat.dto.CursorPageResponse;
import com.be.sportizebe.domain.chat.entity.ChatMessage;
import com.be.sportizebe.domain.chat.entity.ChatRoom;
import com.be.sportizebe.domain.chat.repository.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatMessageService {
    private final ChatMessageRepository messageRepository;

    @Transactional
    public ChatMessage saveChat(ChatRoom room, Long senderUserId, String senderNickname, String content) {
        ChatMessage msg = ChatMessage.chat(room, senderUserId, senderNickname, content);
        return messageRepository.save(msg);
    }
    /**
     *  커서 페이징 통합:
     * - beforeId 없으면 최신 size개 !
     * - beforeId 있으면 해당 id보다 과거 size개
     */
    public CursorPageResponse<ChatMessageResponse> list(Long roomId, Long beforeId, int size) {
        int safeSize = Math.min(Math.max(size, 1), 100); // 1~100 제한(임의)

        // hasNext 판별 위해 size+1개 조회
        PageRequest page = PageRequest.of(0, safeSize + 1);

        List<ChatMessage> fetched = (beforeId == null)
                ? messageRepository.findByRoom_IdOrderByIdDesc(roomId, page)
                : messageRepository.findByRoom_IdAndIdLessThanOrderByIdDesc(roomId, beforeId, page);

        boolean hasNext = fetched.size() > safeSize;

        if (hasNext) {
            fetched = fetched.subList(0, safeSize);
        }

        // 현재는 id desc(최신->과거)로 가져왔는데,
        // UI는 보통 과거 -> 최신으로 보여주기 위해 reverse 사용하는 거임
        Collections.reverse(fetched);

        Long nextCursor = fetched.isEmpty() ? null : fetched.get(0).getId();
        // reverse 했으니 fetched.get(0)가 "이번 응답에서 가장 오래된 메시지"
        // 다음 요청은 beforeId=nextCursor

        List<ChatMessageResponse> items = fetched.stream()
                .map(ChatMessageResponse::from)
                .toList();

        return CursorPageResponse.<ChatMessageResponse>builder()
                .items(items)
                .nextCursor(nextCursor)
                .hasNext(hasNext)
                .build();

    }
}
