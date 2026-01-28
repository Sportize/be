package com.be.sportizebe.domain.post.repository;

import com.be.sportizebe.domain.post.entity.Post;
import com.be.sportizebe.domain.post.entity.PostProperty;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
  Page<Post> findByProperty(PostProperty property, Pageable pageable);
}