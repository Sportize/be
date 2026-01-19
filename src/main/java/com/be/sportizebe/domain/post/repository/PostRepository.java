package com.be.sportizebe.domain.post.repository;

import com.be.sportizebe.domain.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {}