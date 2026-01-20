package com.be.sportizebe.domain.comment.repository;

import com.be.sportizebe.domain.comment.entity.Comment;
import com.be.sportizebe.domain.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    // 게시글의 최상위 댓글 목록 조회 (대댓글 제외)
    List<Comment> findByPostAndParentIsNullOrderByCreatedAtAsc(Post post);

    // 게시글의 전체 댓글 수
    long countByPost(Post post);

    // 특정 댓글의 대댓글 수
    long countByParent(Comment parent);

    // 게시글의 모든 댓글 삭제
    void deleteAllByPost(Post post);
}