package com.be.sportizebe.domain.comment.service;

import com.be.sportizebe.domain.comment.dto.request.CreateCommentRequest;
import com.be.sportizebe.domain.comment.dto.response.CommentResponse;
import com.be.sportizebe.domain.comment.entity.Comment;
import com.be.sportizebe.domain.comment.exception.CommentErrorCode;
import com.be.sportizebe.domain.comment.repository.CommentRepository;
import com.be.sportizebe.domain.post.entity.Post;
import com.be.sportizebe.domain.post.exception.PostErrorCode;
import com.be.sportizebe.domain.post.repository.PostRepository;
import com.be.sportizebe.domain.user.entity.User;
import com.be.sportizebe.global.exception.CustomException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

  private final CommentRepository commentRepository;
  private final PostRepository postRepository;

  @Override
  @Transactional
  public CommentResponse createComment(Long postId, CreateCommentRequest request, User user) {
    // 게시글 조회
    Post post = postRepository.findById(postId)
        .orElseThrow(() -> new CustomException(PostErrorCode.POST_NOT_FOUND));

    // 부모 댓글 조회 (대댓글인 경우)
    Comment parent = null;
    if (request.parentId() != null) {
      parent = commentRepository.findById(request.parentId())
          .orElseThrow(() -> new CustomException(CommentErrorCode.COMMENT_NOT_FOUND));
    }

    // 댓글 생성
    Comment comment = request.toEntity(post, user, parent);
    Comment savedComment = commentRepository.save(comment);

    return CommentResponse.from(savedComment);
  }

  @Override
  public List<CommentResponse> getCommentsByPostId(Long postId) {
    // 게시글 조회
    Post post = postRepository.findById(postId)
        .orElseThrow(() -> new CustomException(PostErrorCode.POST_NOT_FOUND));

    // 최상위 댓글만 조회 (대댓글은 children으로 포함됨)
    List<Comment> comments = commentRepository.findByPostAndParentIsNullOrderByCreatedAtAsc(post);

    return comments.stream()
        .map(CommentResponse::from)
        .toList();
  }

//  @Override
//  @Transactional
//  public void deleteComment(Long commentId, User user) {
//    Comment comment = commentRepository.findById(commentId)
//        .orElseThrow(() -> new CustomException(CommentErrorCode.COMMENT_NOT_FOUND));
//
//    // 작성자 확인
//    if (!comment.getUser().getId().equals(user.getId())) {
//      throw new CustomException(CommentErrorCode.COMMENT_DELETE_DENIED);
//    }
//
//    commentRepository.delete(comment);
//  }

  @Override
  public long getCommentCount(Long postId) {
    Post post = postRepository.findById(postId)
        .orElseThrow(() -> new CustomException(PostErrorCode.POST_NOT_FOUND));

    return commentRepository.countByPost(post);
  }
}