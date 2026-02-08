package com.be.sportizebe.domain.post.repository;

import com.be.sportizebe.domain.post.entity.Post;
import com.be.sportizebe.domain.post.entity.PostProperty;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    Page<Post> findByProperty(PostProperty property, Pageable pageable);

    // 첫 페이지: 11개(10 + 1)
    List<Post> findTop11ByUserIdOrderByIdDesc(Long userId);

    // 다음 페이지: 11개(10 + 1)
    List<Post> findTop11ByUserIdAndIdLessThanOrderByIdDesc(Long userId, Long cursor);

}