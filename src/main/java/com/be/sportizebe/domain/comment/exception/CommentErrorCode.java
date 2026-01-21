package com.be.sportizebe.domain.comment.exception;

import com.be.sportizebe.global.exception.model.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum CommentErrorCode implements BaseErrorCode {
  COMMENT_NOT_FOUND("C001", "댓글을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
  COMMENT_DELETE_DENIED("C002", "댓글 삭제 권한이 없습니다.", HttpStatus.FORBIDDEN),
  COMMENT_UPDATE_DENIED("C003", "댓글 수정 권한이 없습니다.", HttpStatus.FORBIDDEN),
  INVALID_PARENT_COMMENT("C004", "유효하지 않은 부모 댓글입니다.", HttpStatus.BAD_REQUEST),
  COMMENT_PARENT_POST_MISMATCH("C005", "부모 댓글 다른 게시글에 속해있습니다.", HttpStatus.BAD_REQUEST),
  NESTED_REPLY_NOT_ALLOWED("C006", "대댓글에는 답글을 달 수 없습니다.", HttpStatus.BAD_REQUEST);

  private final String code;
  private final String message;
  private final HttpStatus status;
}