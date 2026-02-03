package com.be.sportizebe.domain.post.service;

import com.be.sportizebe.domain.post.dto.request.CreatePostRequest;
import com.be.sportizebe.domain.post.dto.request.UpdatePostRequest;
import com.be.sportizebe.domain.post.dto.response.PostPageResponse;
import com.be.sportizebe.domain.post.dto.response.PostResponse;
import com.be.sportizebe.domain.post.entity.PostProperty;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

public interface PostService {
  PostResponse createPost(PostProperty property, CreatePostRequest request, MultipartFile image, Long userId); // 게시글 생성

  PostResponse updatePost(Long postId, UpdatePostRequest request, Long userId); // 게시글 수정

  void deletePost(Long postId, Long userId); // 게시글 삭제

  PostPageResponse getPosts(PostProperty property, Pageable pageable); // 게시글 목록 조회
}
