package com.be.sportizebe.domain.chat.exception;

import com.be.sportizebe.global.exception.model.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ChatErrorCode implements BaseErrorCode {
  CHAT_ROOM_NOT_FOUND("CHAT_001", "채팅방을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
  SELF_CHAT_NOT_ALLOWED("CHAT_002", "자신의 게시글에는 채팅을 시작할 수 없습니다.", HttpStatus.BAD_REQUEST),
  CHAT_MEMBER_NOT_FOUND("CHAT_003", "채팅방 멤버를 찾을 수 없습니다.", HttpStatus.NOT_FOUND);

  private final String code;
  private final String message;
  private final HttpStatus status;
}
