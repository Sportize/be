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

  /**
   * Create a new comment for a post, optionally as a reply to an existing comment.
   *
   * @param postId ID of the post to attach the comment to
   * @param request DTO containing comment content and optional parentId for a parent comment
   * @param user   author of the new comment
   * @return CommentResponse representation of the saved comment
   * @throws CustomException when the post does not exist (PostErrorCode.POST_NOT_FOUND) or when the specified parent comment does not exist (CommentErrorCode.COMMENT_NOT_FOUND)
   */
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

  /**
   * Retrieve top-level comments for the specified post and map them to CommentResponse objects.
   *
   * @param postId the ID of the post whose top-level comments should be returned
   * @return a list of CommentResponse representing the post's top-level comments; each response includes its nested replies via the `children` field
   * @throws CustomException with PostErrorCode.POST_NOT_FOUND if no post exists with the given `postId`
   */
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
/**
   * Get the number of comments for the specified post.
   *
   * @param postId the ID of the post to count comments for
   * @return the total number of comments associated with the post
   * @throws CustomException if no post exists with the given ID (PostErrorCode.POST_NOT_FOUND)
   */

  @Override
  public long getCommentCount(Long postId) {
    Post post = postRepository.findById(postId)
        .orElseThrow(() -> new CustomException(PostErrorCode.POST_NOT_FOUND));

    return commentRepository.countByPost(post);
  }
}