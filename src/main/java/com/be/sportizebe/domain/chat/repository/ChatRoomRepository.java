package com.be.sportizebe.domain.chat.repository;

import com.be.sportizebe.domain.chat.entity.ChatRoom;
import com.be.sportizebe.domain.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    // 특정 게시글에 대해 두 사용자 간의 1대1 채팅방이 이미 존재하는지 확인
    Optional<ChatRoom> findByPostAndGuestUserId(Post post, Long guestUserId);

    // 사용자가 참여한 1대1 채팅방 목록 조회 (게시글 작성자 또는 채팅 요청자로 참여)
    List<ChatRoom> findByPost_UserIdOrGuestUserId(Long postUserId, Long guestUserId);
}
