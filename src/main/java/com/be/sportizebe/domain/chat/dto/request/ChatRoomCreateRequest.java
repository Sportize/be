package com.be.sportizebe.domain.chat.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class ChatRoomCreateRequest {
    @NotBlank
    @Size(max = 100)
    private String name;


    // 일단 이렇게 하고 나중에 확장하기
    // 1. name -> roomName
    // 2. Description 필드 추가
    // 3. 썸네일 img 필드 추가
}
