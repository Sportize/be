package com.be.sportizebe.domain.club.service;

import com.be.sportizebe.domain.club.dto.request.ClubCreateRequest;
import com.be.sportizebe.domain.club.dto.request.ClubUpdateRequest;
import com.be.sportizebe.domain.club.dto.response.ClubImageResponse;
import com.be.sportizebe.domain.club.dto.response.ClubResponse;
import com.be.sportizebe.domain.user.entity.User;
import org.springframework.web.multipart.MultipartFile;

public interface ClubService {
  ClubResponse createClub(ClubCreateRequest request, MultipartFile image, User user); // 동호회 생성

  ClubResponse updateClub(Long clubId, ClubUpdateRequest request, User user); // 동호회 수정

  ClubImageResponse updateClubImage(Long clubId, MultipartFile image, User user); // 동호회 사진 수정
}
