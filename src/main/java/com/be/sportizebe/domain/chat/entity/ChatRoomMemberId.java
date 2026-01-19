package com.be.sportizebe.domain.chat.entity;

import java.io.Serializable;

public class ChatRoomMemberId implements Serializable {
    // Serializable: 복합키 떄문에 사용하는 거임 !
    // ChatRoomMemberId: 식별자 전용 객체
    // ChatRoomMember: 엔티티(데이터 + 행위)

    private Long room;
    private Long userId;
}
