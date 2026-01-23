package com.be.sportizebe.domain.chat.repository;

import com.be.sportizebe.domain.chat.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
}
