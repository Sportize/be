package com.be.sportizebe.domain.post.exception;

import com.be.sportizebe.global.exception.model.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum PostErrorCode implements BaseErrorCode {
  POST_NOT_FOUND("P001", "게시글을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
  POST_ACCESS_DENIED("P002", "게시글 접근 권한이 없습니다.", HttpStatus.FORBIDDEN),
  POST_UPDATE_DENIED("P003", "게시글 수정 권한이 없습니다.", HttpStatus.FORBIDDEN),
  POST_DELETE_DENIED("P004", "게시글 삭제 권한이 없습니다.", HttpStatus.FORBIDDEN),
  POST_ALREADY_DELETED("P005", "이미 삭제된 게시글입니다.", HttpStatus.BAD_REQUEST),
  INVALID_POST_PROPERTY("P006", "잘못된 게시판 속성입니다.", HttpStatus.BAD_REQUEST);

  private final String code;
  private final String message;
  private final HttpStatus status;
}
