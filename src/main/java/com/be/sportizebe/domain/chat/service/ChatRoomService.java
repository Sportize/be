package com.be.sportizebe.domain.chat.service;

import com.be.sportizebe.domain.chat.entity.ChatRoom;
import com.be.sportizebe.domain.chat.exception.ChatErrorCode;
import com.be.sportizebe.domain.chat.repository.ChatRoomRepository;
import com.be.sportizebe.domain.post.entity.Post;
import com.be.sportizebe.domain.user.entity.User;
import com.be.sportizebe.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;

    @Transactional
    public ChatRoom createGroup(String name) {
        ChatRoom room = ChatRoom.builder()
                .name(name)
                .chatRoomType(ChatRoom.ChatRoomType.GROUP)
                .build();
        return chatRoomRepository.save(room);
    }

    @Transactional
    public ChatRoom createNote(Post post, User guestUser) {
        User hostUser = post.getUser(); // 게시글 등록자

        // 자기 자신에게 채팅 불가
        if (hostUser.getId() == guestUser.getId()) {
            throw new CustomException(ChatErrorCode.SELF_CHAT_NOT_ALLOWED);
        }

        // 이미 존재하는 채팅방이 있으면 반환
        return chatRoomRepository.findByPostAndHostUserAndGuestUser(post, hostUser, guestUser)
                .orElseGet(() -> {
                    ChatRoom room = ChatRoom.builder()
                            .name(post.getTitle())
                            .chatRoomType(ChatRoom.ChatRoomType.NOTE)
                            .maxMembers(2)
                            .post(post)
                            .hostUser(hostUser)
                            .guestUser(guestUser)
                            .build();
                    return chatRoomRepository.save(room);
                });
    }

    // 사용자가 참여한 1대1 채팅방 목록 조회
    public List<ChatRoom> findMyNoteRooms(User user) {
        return chatRoomRepository.findByHostUserOrGuestUser(user, user);
    }

    public List<ChatRoom> findAll() {
        return chatRoomRepository.findAll();
    }

    public ChatRoom getOrThrow(Long roomId) {
        return chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new CustomException(ChatErrorCode.CHAT_ROOM_NOT_FOUND));
    }
}
