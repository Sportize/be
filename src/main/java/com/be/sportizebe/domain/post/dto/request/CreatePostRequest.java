package com.be.sportizebe.domain.post.dto.request;

import com.be.sportizebe.domain.post.entity.Post;
import com.be.sportizebe.domain.post.entity.PostProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record CreatePostRequest(
    @NotBlank(message = "제목은 필수입니다.") String title,
    @NotBlank(message = "내용은 필수입니다.") String content,
    boolean isAnonymous,
    String imgUrl) {

  public Post toEntity(PostProperty property) { // DTO -> Entity 변환
    return Post.builder()
        .title(title)
        .content(content)
        .isAnonymous(isAnonymous)
        .imgUrl(imgUrl)
        .property(property)
        .build();
  }
}
