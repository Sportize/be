package com.be.sportizebe.domain.chat.repository;

import com.be.sportizebe.domain.chat.entity.ChatMessage;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    // 최초 로딩: 최신 N개 (id desc)
    List<ChatMessage> findByRoom_IdOrderByIdDesc(Long roomId, Pageable pageable);

    // 커서 로딩: beforeId 보다 과거 N개 (id desc)
    List<ChatMessage> findByRoom_IdAndIdLessThanOrderByIdDesc(Long roomId, Long beforeId, Pageable pageable);
}
