package com.be.sportizebe.domain.comment.service;

import com.be.sportizebe.domain.comment.dto.request.CreateCommentRequest;
import com.be.sportizebe.domain.comment.dto.response.CommentResponse;
import com.be.sportizebe.domain.user.entity.User;

import java.util.List;

public interface CommentService {

  /**
 * Create a new comment on the specified post, including replies to an existing comment.
 *
 * @param postId  identifier of the post to attach the comment to
 * @param request DTO containing the comment content and optional parent comment id for replies
 * @param user    user creating the comment
 * @return        the created CommentResponse representing the saved comment
 */
  CommentResponse createComment(Long postId, CreateCommentRequest request, User user);

  /**
 * Retrieves the comments associated with a specific post.
 *
 * @param postId the identifier of the post whose comments are requested
 * @return a list of CommentResponse objects representing the post's comments (empty if none)
 */
  List<CommentResponse> getCommentsByPostId(Long postId);

  // 댓글 삭제
  // void deleteComment(Long commentId, User user);

  /**
 * Retrieves the total number of comments associated with a post.
 *
 * @param postId identifier of the post whose comments are counted
 * @return the total number of comments for the specified post
 */
  long getCommentCount(Long postId);
}