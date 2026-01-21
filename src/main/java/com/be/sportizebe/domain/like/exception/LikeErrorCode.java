package com.be.sportizebe.domain.like.exception;

import com.be.sportizebe.global.exception.model.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum LikeErrorCode implements BaseErrorCode {
  LIKE_NOT_FOUND("L001", "좋아요를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
  ALREADY_LIKED("L002", "이미 좋아요를 눌렀습니다.", HttpStatus.BAD_REQUEST),
  INVALID_TARGET_TYPE("L003", "잘못된 좋아요 대상 타입입니다.", HttpStatus.BAD_REQUEST),
  TARGET_NOT_FOUND("L004", "좋아요 대상을 찾을 수 없습니다.", HttpStatus.NOT_FOUND);

  private final String code;
  private final String message;
  private final HttpStatus status;
}