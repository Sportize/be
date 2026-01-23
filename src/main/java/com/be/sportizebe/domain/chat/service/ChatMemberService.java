package com.be.sportizebe.domain.chat.service;

import com.be.sportizebe.domain.chat.entity.ChatRoom;
import com.be.sportizebe.domain.chat.entity.ChatRoomMember;
import com.be.sportizebe.domain.chat.repository.ChatRoomMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatMemberService {
    private final ChatRoomMemberRepository memberRepository;
    // 클래스에 Transactional 준 건 조회 전용 트랜잭션
    // 아래의 Transactional로 다시 덮어씀으로써 쓰기 전용 트랜잭션
    @Transactional
    public void join(ChatRoom room, Long userId, ChatRoomMember.Role role){
        memberRepository.findByRoom_IdAndUserId(room.getId(), userId)
                .ifPresentOrElse(
                        member -> {
                            if (!member.isActiveMember()) member.rejoin();
                        },
                        () -> {
                            ChatRoomMember m = ChatRoomMember.builder()
                                    .room(room)
                                    .userId(userId)
                                    .role(role == null ? ChatRoomMember.Role.MEMBER : role)
                                    .build();
                            memberRepository.save(m);
                        }
                );
    }

    public boolean isActiveMember(Long roomId, Long userId){
        return memberRepository.existsByRoom_IdAndUserIdAndLeftAtIsNull(roomId, userId);
    }
    @Transactional
    public void leave(Long roomId, Long userId){
        ChatRoomMember member = memberRepository.findByRoom_IdAndUserId(roomId, userId)
                .orElseThrow(() -> new IllegalArgumentException("member not found"));
        if (member.isActiveMember()) member.leave();
    }
}
