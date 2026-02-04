package com.be.sportizebe.domain.notification.service;

import com.be.sportizebe.domain.notification.dto.response.JoinClubRequestResponse;

import java.util.List;

public interface JoinClubRequestService {

  // 가입 신청
  JoinClubRequestResponse requestJoin(Long clubId, Long userId);

  // 가입 신청 취소
  void cancelRequest(Long requestId, Long userId);

  // 가입 승인 (동호회장만)
  JoinClubRequestResponse approveRequest(Long requestId, Long leaderId);

  // 가입 거절 (동호회장만)
  JoinClubRequestResponse rejectRequest(Long requestId, Long leaderId);

  // 동호회의 대기 중인 가입 신청 목록 조회 (동호회장만)
  List<JoinClubRequestResponse> getPendingRequests(Long clubId, Long leaderId);

  // 내 가입 신청 목록 조회
  List<JoinClubRequestResponse> getMyRequests(Long userId);
}