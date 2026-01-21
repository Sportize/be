package com.be.sportizebe.domain.comment.dto.response;

import com.be.sportizebe.domain.comment.entity.Comment;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Schema(title = "CommentResponse DTO", description = "댓글 응답")
public record CommentResponse(
    @Schema(description = "댓글 ID", example = "1")
    Long commentId,

    @Schema(description = "댓글 내용", example = "댓글 내용입니다.")
    String content,

    @Schema(description = "작성자 이름", example = "username")
    String username,

    @Schema(description = "작성자 ID", example = "1")
    Long userId,

    @Schema(description = "작성일시")
    LocalDateTime createdAt,

    @Schema(description = "대댓글 목록")
    List<CommentResponse> children,

    @Schema(description = "대댓글 수", example = "3")
    int childrenCount) {

  public static CommentResponse from(Comment comment) {
    List<CommentResponse> childResponses = comment.getChildren().stream()
        .map(CommentResponse::from)
        .toList();

    return CommentResponse.builder()
        .commentId(comment.getId())
        .content(comment.getContent())
        .username(comment.getUser().getUsername())
        .userId(comment.getUser().getId())
        .createdAt(comment.getCreatedAt())
        .children(childResponses)
        .childrenCount(childResponses.size())
        .build();
  }
}