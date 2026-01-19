package com.be.sportizebe.domain.chat.service;

import com.be.sportizebe.domain.chat.entity.ChatRoom;
import com.be.sportizebe.domain.chat.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true) // all or nothing !
public class ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;

    @Transactional
    public ChatRoom create(String name){
        ChatRoom room = ChatRoom.builder()
                .name(name)
                .build();
        return chatRoomRepository.save(room);
    }

    public List<ChatRoom> findAll(){
        return chatRoomRepository.findAll();
    }

    public ChatRoom getOrThrow(Long roomId) {
        return chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("chat room not found: " + roomId));
    }
}
