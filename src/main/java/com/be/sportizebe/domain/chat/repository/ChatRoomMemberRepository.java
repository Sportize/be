package com.be.sportizebe.domain.chat.repository;

import com.be.sportizebe.domain.chat.entity.ChatRoomMember;
import com.be.sportizebe.domain.chat.entity.ChatRoomMemberId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomMemberRepository extends JpaRepository<ChatRoomMember, ChatRoomMemberId> {
}
