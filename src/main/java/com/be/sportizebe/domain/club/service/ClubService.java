package com.be.sportizebe.domain.club.service;

import com.be.sportizebe.domain.club.dto.request.ClubCreateRequest;
import com.be.sportizebe.domain.club.dto.request.ClubUpdateRequest;
import com.be.sportizebe.domain.club.dto.response.ClubDetailResponse;
import com.be.sportizebe.domain.club.dto.response.ClubImageResponse;
import com.be.sportizebe.domain.club.dto.response.ClubResponse;
import com.be.sportizebe.domain.club.dto.response.ClubScrollResponse;
import com.be.sportizebe.domain.user.entity.User;
import org.springframework.web.multipart.MultipartFile;

public interface ClubService {
    ClubResponse createClub(ClubCreateRequest request, MultipartFile image, User user); // 동호회 생성

    ClubResponse updateClub(Long clubId, ClubUpdateRequest request, User user); // 동호회 수정

    ClubImageResponse updateClubImage(Long clubId, MultipartFile image, User user); // 동호회 사진 수정

    ClubDetailResponse getClub(Long clubId); // 동호회 개별 조회

    ClubScrollResponse getClubsByScroll(Long cursor, int size); // 동호회 전체 조회 (무한 스크롤)

    ClubScrollResponse getMyClubsByScroll(Long cursor, int size, User user); // 내가 가입한 동호회 조회 (무한 스크롤)

}
