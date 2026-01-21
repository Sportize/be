package com.be.sportizebe.domain.comment.service;

import com.be.sportizebe.domain.comment.dto.request.CreateCommentRequest;
import com.be.sportizebe.domain.comment.dto.response.CommentResponse;
import com.be.sportizebe.domain.user.entity.User;

import java.util.List;

public interface CommentService {

  // 댓글 생성 (대댓글 포함)
  CommentResponse createComment(Long postId, CreateCommentRequest request, User user);

  // 게시글의 댓글 목록 조회
  List<CommentResponse> getCommentsByPostId(Long postId);

  // 댓글 삭제
  void deleteComment(Long commentId, User user);

  // 게시글의 댓글 수 조회
  long getCommentCount(Long postId);
}