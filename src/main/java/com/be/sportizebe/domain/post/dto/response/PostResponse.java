package com.be.sportizebe.domain.post.dto.response;

import com.be.sportizebe.domain.post.entity.Post;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(title = "PostResponse DTO", description = "게시글 관련 응답")
public record PostResponse(
    @Schema(description = "게시글 고유번호", example = "1") Long postId,
    @Schema(description = "게시글 제목", example = "게시글 제목 ...") String title,
    @Schema(description = "게시글 내용", example = "게시글 글 내용 ...") String content,
    @Schema(description = "게시글 등록자", example = "사용자명") String publisher,
    @Schema(description = "게시글 사진 url", example = "s3 url") String imgUrl,
    @Schema(description = "익명 여부", example = "true:익명 / false:사용자명") String isAnonymous) {

  public static PostResponse from(Post post) { // Post -> Response 변환
    return PostResponse.builder()
        .postId(post.getId())
        .title(post.getTitle())
        .content(post.getContent())
        .publisher(post.isAnonymous() ? "익명" : "사용자명") // TODO: User 매핑 필요
        .imgUrl(post.getImgUrl())
        .isAnonymous(post.isAnonymous() ? "익명" : "사용자명")
        .build();
  }
}
