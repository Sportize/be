package com.be.sportizebe.domain.chat.dto.response;

import com.be.sportizebe.domain.chat.entity.ChatRoom;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ChatRoomResponse {
    private Long roomId;
    private String name;
    private boolean active;
    private String chatRoomType;

    // 1대1 채팅방(쪽지)용 필드
    private Long postId;
    private Long hostUserId;
    private String hostUsername;
    private Long guestUserId;
    private String guestUsername;

    public static ChatRoomResponse from(ChatRoom r) {
        ChatRoomResponseBuilder builder = ChatRoomResponse.builder()
                .roomId(r.getId())
                .active(r.isActive())
                .chatRoomType(r.getChatRoomType() != null ? r.getChatRoomType().name() : null);

        // 타입별 name 파생
        if (r.getChatRoomType() == ChatRoom.ChatRoomType.NOTE && r.getPost() != null) {
            builder.name(r.getPost().getTitle());
            builder.postId(r.getPost().getId());
            // hostUser는 게시글 작성자로 파생
            builder.hostUserId(r.getPost().getUser().getId())
                   .hostUsername(r.getPost().getUser().getUsername());
        } else if (r.getChatRoomType() == ChatRoom.ChatRoomType.GROUP && r.getClub() != null) {
            builder.name(r.getClub().getName());
        }

        if (r.getGuestUser() != null) {
            builder.guestUserId(r.getGuestUser().getId())
                   .guestUsername(r.getGuestUser().getUsername());
        }

        return builder.build();
    }
}
