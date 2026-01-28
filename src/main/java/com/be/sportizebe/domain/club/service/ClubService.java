package com.be.sportizebe.domain.club.service;

import com.be.sportizebe.domain.club.dto.request.ClubCreateRequest;
import com.be.sportizebe.domain.club.dto.response.ClubResponse;
import com.be.sportizebe.domain.user.entity.SportType;
import com.be.sportizebe.domain.user.entity.User;

public interface ClubService {
  ClubResponse createClub(ClubCreateRequest request, User user); // 동호회 생성
}
