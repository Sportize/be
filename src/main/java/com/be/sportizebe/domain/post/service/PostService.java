package com.be.sportizebe.domain.post.service;

import com.be.sportizebe.domain.post.dto.request.CreatePostRequest;
import com.be.sportizebe.domain.post.dto.response.CreatePostResponse;
import com.be.sportizebe.domain.post.entity.PostProperty;

public interface PostService {
  /**
 * Create a new post using the provided properties and request data.
 *
 * @param property post properties and contextual metadata for the creation operation
 * @param request  data required to create the post (content, attachments, tags, etc.)
 * @return         the result of the creation containing the created post's identifier and related metadata
 */
CreatePostResponse createPost(PostProperty property, CreatePostRequest request); // 게시글 생성
}