package com.be.sportizebe.domain.like.controller;

import com.be.sportizebe.domain.like.dto.response.LikeResponse;
import com.be.sportizebe.domain.like.entity.LikeTargetType;
import com.be.sportizebe.domain.like.service.LikeService;
import com.be.sportizebe.domain.user.entity.User;
import com.be.sportizebe.global.response.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/likes")
@Tag(name = "like", description = "좋아요 관련 API")
public class LikeController {

    private final LikeService likeService;

    @PostMapping("/posts/{postId}")
    @Operation(summary = "게시글 좋아요 토글", description = "게시글 좋아요 추가/취소")
    public ResponseEntity<BaseResponse<LikeResponse>> togglePostLike(
        @AuthenticationPrincipal User user,
        @Parameter(description = "게시글 ID") @PathVariable Long postId) {
        LikeResponse response = likeService.toggleLike(user, LikeTargetType.POST, postId);
        return ResponseEntity.ok(BaseResponse.success("좋아요 처리 완료", response));
    }

    @PostMapping("/comments/{commentId}")
    @Operation(summary = "댓글 좋아요 토글", description = "댓글 좋아요 추가/취소")
    public ResponseEntity<BaseResponse<LikeResponse>> toggleCommentLike(
        @AuthenticationPrincipal User user,
        @Parameter(description = "댓글 ID") @PathVariable Long commentId) {
        LikeResponse response = likeService.toggleLike(user, LikeTargetType.COMMENT, commentId);
        return ResponseEntity.ok(BaseResponse.success("좋아요 처리 완료", response));
    }

    @GetMapping("/posts/{postId}")
    @Operation(summary = "게시글 좋아요 수 조회", description = "게시글의 좋아요 수와 본인 좋아요 여부 조회")
    public ResponseEntity<BaseResponse<LikeResponse>> getPostLikeStatus(
        @AuthenticationPrincipal User user,
        @Parameter(description = "게시글 ID") @PathVariable Long postId) {
        boolean liked = likeService.isLiked(user, LikeTargetType.POST, postId);
        long likeCount = likeService.getLikeCount(LikeTargetType.POST, postId);
        LikeResponse response = LikeResponse.of(liked, LikeTargetType.POST, postId, likeCount);
        return ResponseEntity.ok(BaseResponse.success(response));
    }

    @GetMapping("/comments/{commentId}")
    @Operation(summary = "댓글 좋아요 수 조회", description = "댓글의 좋아요 수와 본인 좋아요 여부 조회")
    public ResponseEntity<BaseResponse<LikeResponse>> getCommentLikeStatus(
        @AuthenticationPrincipal User user,
        @Parameter(description = "댓글 ID") @PathVariable Long commentId) {
        boolean liked = likeService.isLiked(user, LikeTargetType.COMMENT, commentId);
        long likeCount = likeService.getLikeCount(LikeTargetType.COMMENT, commentId);
        LikeResponse response = LikeResponse.of(liked, LikeTargetType.COMMENT, commentId, likeCount);
        return ResponseEntity.ok(BaseResponse.success(response));
    }
}
