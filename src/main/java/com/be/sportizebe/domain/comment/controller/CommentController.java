package com.be.sportizebe.domain.comment.controller;

import com.be.sportizebe.domain.comment.dto.request.CreateCommentRequest;
import com.be.sportizebe.domain.comment.dto.response.CommentListResponse;
import com.be.sportizebe.domain.comment.dto.response.CommentResponse;
import com.be.sportizebe.domain.comment.service.CommentService;
import com.be.sportizebe.global.response.BaseResponse;
import com.be.sportizebe.global.cache.dto.UserAuthInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts/{postId}/comments")
@Tag(name = "comment", description = "댓글 관련 API")
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    @Operation(summary = "댓글 생성", description = "게시글에 댓글 또는 대댓글 생성")
    public ResponseEntity<BaseResponse<CommentResponse>> createComment(
        @Parameter(description = "게시글 ID") @PathVariable Long postId,
        @RequestBody @Valid CreateCommentRequest request,
        @AuthenticationPrincipal UserAuthInfo userAuthInfo) {
        CommentResponse response = commentService.createComment(postId, request, userAuthInfo.getId());
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(BaseResponse.success("댓글 생성 성공", response));
    }

    @GetMapping
    @Operation(summary = "댓글 목록 조회", description = "게시글의 댓글 목록 조회 (대댓글 포함)")
    public ResponseEntity<BaseResponse<CommentListResponse>> getComments(
        @Parameter(description = "게시글 ID") @PathVariable Long postId) {
        CommentListResponse response = commentService.getCommentsByPostId(postId);
        return ResponseEntity.ok(BaseResponse.success(response));
    }

    @DeleteMapping("/{commentId}")
    @Operation(summary = "댓글 삭제", description = "댓글 삭제 (대댓글도 함께 삭제)")
    public ResponseEntity<BaseResponse<Void>> deleteComment(
        @Parameter(description = "게시글 ID") @PathVariable Long postId,
        @Parameter(description = "댓글 ID") @PathVariable Long commentId,
        @AuthenticationPrincipal UserAuthInfo userAuthInfo) {
        commentService.deleteComment(postId, commentId, userAuthInfo.getId());
        return ResponseEntity.ok(BaseResponse.success("댓글 삭제 성공", null));
    }

    @GetMapping("/count")
    @Operation(summary = "댓글 수 조회", description = "게시글의 총 댓글 수 조회")
    public ResponseEntity<BaseResponse<Long>> getCommentCount(
        @Parameter(description = "게시글 ID") @PathVariable Long postId) {
        long count = commentService.getCommentCount(postId);
        return ResponseEntity.ok(BaseResponse.success(count));
    }
}
