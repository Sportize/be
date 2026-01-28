package com.be.sportizebe.domain.club.exception;

import com.be.sportizebe.global.exception.model.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ClubErrorCode implements BaseErrorCode {
  CLUB_NOT_FOUND("CLUB_001", "동호회를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
  CLUB_NAME_DUPLICATED("CLUB_002", "이미 존재하는 동호회 이름입니다.", HttpStatus.CONFLICT),
  CLUB_UPDATE_DENIED("CLUB_003", "동호회 수정 권한이 없습니다.", HttpStatus.FORBIDDEN),
  CLUB_MAX_MEMBERS_TOO_SMALL("CLUB_004", "최대 정원은 현재 참여 인원보다 적을 수 없습니다.", HttpStatus.BAD_REQUEST);

  private final String code;
  private final String message;
  private final HttpStatus status;
}
