package com.be.sportizebe.domain.post.service;

import com.be.sportizebe.domain.post.dto.request.CreatePostRequest;
import com.be.sportizebe.domain.post.dto.response.CreatePostResponse;
import com.be.sportizebe.domain.post.entity.PostProperty;

public interface PostService {
  CreatePostResponse createPost(PostProperty property, CreatePostRequest request); // 게시글 생성
}
