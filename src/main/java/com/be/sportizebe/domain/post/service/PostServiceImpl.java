package com.be.sportizebe.domain.post.service;

import com.be.sportizebe.domain.post.dto.request.CreatePostRequest;
import com.be.sportizebe.domain.post.dto.response.CreatePostResponse;
import com.be.sportizebe.domain.post.entity.Post;
import com.be.sportizebe.domain.post.entity.PostProperty;
import com.be.sportizebe.domain.post.repository.PostRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

  private final PostRepository postRepository;

  /**
   * Creates a new Post from the given request and properties, persists it, and returns a response DTO.
   *
   * @param property attributes applied to the created Post entity
   * @param request  data used to construct the Post entity
   * @return a {@code CreatePostResponse} representing the persisted Post
   */
  @Override
  @Transactional
  public CreatePostResponse createPost(PostProperty property, CreatePostRequest request) {
    Post post = request.toEntity(property); // 요청 dto 데이터를 entity로 변환

    Post savedPost = postRepository.save(post); // db에 저장

    return CreatePostResponse.from(savedPost); // entity를 dto로 변환하여 응답
  }
}