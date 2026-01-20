package com.be.sportizebe.domain.comment.repository;

import com.be.sportizebe.domain.comment.entity.Comment;
import com.be.sportizebe.domain.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    /**
 * Retrieve top-level comments for the given post ordered by creation time ascending.
 *
 * @param post the post whose top-level (non-reply) comments to fetch
 * @return a list of top-level Comment entities for the specified post ordered by `createdAt` ascending
 */
    List<Comment> findByPostAndParentIsNullOrderByCreatedAtAsc(Post post);

    /**
 * Count comments associated with a post.
 *
 * @param post the post to count comments for
 * @return the total number of comments associated with the given post
 */
    long countByPost(Post post);

    /**
 * Count replies for the specified parent comment.
 *
 * @param parent the parent Comment whose direct child comments (replies) are counted
 * @return the number of replies to the given parent comment
 */
    long countByParent(Comment parent);

    /**
 * Deletes all comments associated with the given post.
 *
 * @param post the post whose associated comments should be removed
 */
    void deleteAllByPost(Post post);
}