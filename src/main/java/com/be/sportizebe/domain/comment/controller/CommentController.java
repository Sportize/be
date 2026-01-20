package com.be.sportizebe.domain.comment.controller;

import com.be.sportizebe.domain.comment.dto.request.CreateCommentRequest;
import com.be.sportizebe.domain.comment.dto.response.CommentResponse;
import com.be.sportizebe.domain.comment.service.CommentService;
import com.be.sportizebe.domain.user.entity.User;
import com.be.sportizebe.global.response.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts/{postId}/comments")
@Tag(name = "comment", description = "댓글 관련 API")
public class CommentController {

    private final CommentService commentService;

    /**
     * Create a comment or a reply for the specified post.
     *
     * @param postId the ID of the post to which the comment or reply will be added
     * @param request the request payload containing comment content and optional parent comment information
     * @param user the authenticated user creating the comment
     * @return ResponseEntity containing a BaseResponse with the created CommentResponse and a success message; returns HTTP 201 Created on success
     */
    @PostMapping
    @Operation(summary = "댓글 생성", description = "게시글에 댓글 또는 대댓글 생성")
    public ResponseEntity<BaseResponse<CommentResponse>> createComment(
        @Parameter(description = "게시글 ID") @PathVariable Long postId,
        @RequestBody @Valid CreateCommentRequest request,
        @AuthenticationPrincipal User user) {
        CommentResponse response = commentService.createComment(postId, request, user);
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(BaseResponse.success("댓글 생성 성공", response));
    }

    /**
     * Retrieves the comments for the specified post, including nested replies.
     *
     * @param postId the ID of the post whose comments are requested
     * @return an HTTP 200 response containing a BaseResponse with a list of CommentResponse objects for the post
     */
    @GetMapping
    @Operation(summary = "댓글 목록 조회", description = "게시글의 댓글 목록 조회 (대댓글 포함)")
    public ResponseEntity<BaseResponse<List<CommentResponse>>> getComments(
        @Parameter(description = "게시글 ID") @PathVariable Long postId) {
        List<CommentResponse> responses = commentService.getCommentsByPostId(postId);
        return ResponseEntity.ok(BaseResponse.success(responses));
    }

//    @DeleteMapping("/{commentId}")
//    @Operation(summary = "댓글 삭제", description = "댓글 삭제 (대댓글도 함께 삭제)")
//    public ResponseEntity<BaseResponse<Void>> deleteComment(
//        @Parameter(description = "게시글 ID") @PathVariable Long postId,
//        @Parameter(description = "댓글 ID") @PathVariable Long commentId,
//        @AuthenticationPrincipal User user) {
//        commentService.deleteComment(commentId, user);
//        return ResponseEntity.ok(BaseResponse.success("댓글 삭제 성공", null));
/**
     * Retrieve the total number of comments for the specified post.
     *
     * @param postId the ID of the post to count comments for
     * @return the total number of comments for the post
     */

    @GetMapping("/count")
    @Operation(summary = "댓글 수 조회", description = "게시글의 총 댓글 수 조회")
    public ResponseEntity<BaseResponse<Long>> getCommentCount(
        @Parameter(description = "게시글 ID") @PathVariable Long postId) {
        long count = commentService.getCommentCount(postId);
        return ResponseEntity.ok(BaseResponse.success(count));
    }
}