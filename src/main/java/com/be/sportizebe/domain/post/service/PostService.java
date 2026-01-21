package com.be.sportizebe.domain.post.service;

import com.be.sportizebe.domain.post.dto.request.CreatePostRequest;
import com.be.sportizebe.domain.post.dto.request.UpdatePostRequest;
import com.be.sportizebe.domain.post.dto.response.CreatePostResponse;
import com.be.sportizebe.domain.post.entity.PostProperty;
import com.be.sportizebe.domain.user.entity.User;

public interface PostService {
  CreatePostResponse createPost(PostProperty property, CreatePostRequest request); // 게시글 생성

  CreatePostResponse updatePost(Long postId, UpdatePostRequest request, User user); // 게시글 수정

  void deletePost(Long postId, User user); // 게시글 삭제
}
