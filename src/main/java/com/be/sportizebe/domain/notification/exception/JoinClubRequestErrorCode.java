package com.be.sportizebe.domain.notification.exception;

import com.be.sportizebe.global.exception.model.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum JoinClubRequestErrorCode implements BaseErrorCode {
  JOIN_REQUEST_NOT_FOUND("JOIN_001", "가입 신청을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
  JOIN_REQUEST_ALREADY_EXISTS("JOIN_002", "이미 가입 신청한 동호회입니다.", HttpStatus.CONFLICT),
  ALREADY_CLUB_MEMBER("JOIN_003", "이미 가입된 동호회입니다.", HttpStatus.CONFLICT),
  JOIN_REQUEST_NOT_PENDING("JOIN_004", "대기 중인 가입 신청이 아닙니다.", HttpStatus.BAD_REQUEST),
  CANNOT_JOIN_OWN_CLUB("JOIN_005", "자신이 만든 동호회에는 가입 신청할 수 없습니다.", HttpStatus.BAD_REQUEST),
  CLUB_FULL("JOIN_006", "동호회 정원이 가득 찼습니다.", HttpStatus.BAD_REQUEST);

  private final String code;
  private final String message;
  private final HttpStatus status;
}