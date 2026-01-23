package com.be.sportizebe.domain.chat.repository;

import com.be.sportizebe.domain.chat.entity.ChatRoomMember;
import com.be.sportizebe.domain.chat.entity.ChatRoomMemberId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChatRoomMemberRepository
        extends JpaRepository<ChatRoomMember, ChatRoomMemberId> {

    Optional<ChatRoomMember> findByRoom_IdAndUserId(Long roomId, Long userId);

    List<ChatRoomMember> findAllByRoom_IdAndLeftAtIsNull(Long roomId);

    boolean existsByRoom_IdAndUserIdAndLeftAtIsNull(Long roomId, Long userId);
}
