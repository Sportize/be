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
    private Integer maxMembers;

    // 1대1 채팅방(쪽지)용 필드
    private Long postId;
    private Long hostUserId;
    private String hostUsername;
    private Long guestUserId;
    private String guestUsername;

    public static ChatRoomResponse from(ChatRoom r) {
        ChatRoomResponseBuilder builder = ChatRoomResponse.builder()
                .roomId(r.getId())
                .name(r.getName())
                .active(r.isActive())
                .chatRoomType(r.getChatRoomType() != null ? r.getChatRoomType().name() : null)
                .maxMembers(r.getMaxMembers());

        // 1대1 채팅방인 경우 추가 정보 설정
        if (r.getPost() != null) {
            builder.postId(r.getPost().getId());
        }
        if (r.getHostUser() != null) {
            builder.hostUserId(r.getHostUser().getId())
                   .hostUsername(r.getHostUser().getUsername());
        }
        if (r.getGuestUser() != null) {
            builder.guestUserId(r.getGuestUser().getId())
                   .guestUsername(r.getGuestUser().getUsername());
        }

        return builder.build();
    }
}
