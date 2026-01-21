package com.be.sportizebe.domain.comment.dto.request;

import com.be.sportizebe.domain.comment.entity.Comment;
import com.be.sportizebe.domain.post.entity.Post;
import com.be.sportizebe.domain.user.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
@Schema(title = "CreateCommentRequest DTO", description = "댓글 생성 요청")
public record CreateCommentRequest(
    @NotBlank(message = "댓글 내용은 필수입니다.")
    @Schema(description = "댓글 내용", example = "댓글 내용입니다.")
    String content,

    @Schema(description = "부모 댓글 ID (대댓글인 경우)", example = "1")
    Long parentId) {

  public Comment toEntity(Post post, User user, Comment parent) {
    return Comment.builder()
        .content(content)
        .post(post)
        .user(user)
        .parent(parent)
        .build();
  }
}