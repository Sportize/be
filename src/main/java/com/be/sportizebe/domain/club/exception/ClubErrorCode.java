package com.be.sportizebe.domain.club.exception;

import com.be.sportizebe.global.exception.model.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ClubErrorCode implements BaseErrorCode {
  CLUB_NOT_FOUND("CLUB_001", "동호회를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
  CLUB_NAME_DUPLICATED("CLUB_002", "이미 존재하는 동호회 이름입니다.", HttpStatus.CONFLICT);

  private final String code;
  private final String message;
  private final HttpStatus status;
}
