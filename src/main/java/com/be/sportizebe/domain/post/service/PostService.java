package com.be.sportizebe.domain.post.service;

import com.be.sportizebe.domain.post.dto.request.CreatePostRequest;
import com.be.sportizebe.domain.post.dto.request.UpdatePostRequest;
import com.be.sportizebe.domain.post.dto.response.CursorPageResponse;
import com.be.sportizebe.domain.post.dto.response.PostPageResponse;
import com.be.sportizebe.domain.post.dto.response.PostResponse;
import com.be.sportizebe.domain.post.entity.PostProperty;
import com.be.sportizebe.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

public interface PostService {
    PostResponse createPost(PostProperty property, CreatePostRequest request, MultipartFile image, User user); // 게시글 생성

    PostResponse updatePost(Long postId, UpdatePostRequest request, User user); // 게시글 수정

    void deletePost(Long postId, User user); // 게시글 삭제

    PostPageResponse getPosts(PostProperty property, Pageable pageable); // 게시글 목록 조회

    CursorPageResponse<PostResponse> getMyPostsCursor(User user, Long cursor); // 내가 쓴 게시글 무한 스크롤 조회
}

