package com.be.sportizebe.domain.post.service;

import com.be.sportizebe.domain.post.dto.request.CreatePostRequest;
import com.be.sportizebe.domain.post.dto.request.UpdatePostRequest;
import com.be.sportizebe.domain.post.dto.response.PostPageResponse;
import com.be.sportizebe.domain.post.dto.response.PostResponse;
import com.be.sportizebe.domain.post.entity.Post;
import com.be.sportizebe.domain.post.entity.PostProperty;
import com.be.sportizebe.domain.post.exception.PostErrorCode;
import com.be.sportizebe.domain.post.repository.PostRepository;
import com.be.sportizebe.domain.user.entity.User;
import com.be.sportizebe.global.exception.CustomException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;

    @Override
    @CacheEvict(cacheNames = "postList", allEntries = true)
    @Transactional
    public PostResponse createPost(PostProperty property, CreatePostRequest request, User user) {
        Post post = request.toEntity(property, user); // 요청 dto 데이터를 entity로 변환

        Post savedPost = postRepository.save(post); // db에 저장

        return PostResponse.from(savedPost); // entity를 dto로 변환하여 응답
    }

    @Override
    @CacheEvict(cacheNames = "postList", allEntries = true)
    @Transactional
    public void deletePost(Long postId, User user) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(PostErrorCode.POST_NOT_FOUND));

        // 작성자 확인
        if (post.getUser().getId() != user.getId()) {
            throw new CustomException(PostErrorCode.POST_DELETE_DENIED);
        }

        postRepository.delete(post);
    }

    @Override
    @CacheEvict(cacheNames = "postList", allEntries = true)
    @Transactional
    public PostResponse updatePost(Long postId, UpdatePostRequest request, User user) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(PostErrorCode.POST_NOT_FOUND));

        // 작성자 확인
        if (post.getUser().getId() != user.getId()) {
            throw new CustomException(PostErrorCode.POST_UPDATE_DENIED);
        }

        post.update(request.title(), request.content(), request.imgUrl());

        return PostResponse.from(post);
    }

    @Cacheable(cacheNames = "postList", keyGenerator = "postListKeyGenerator")
    @Override
    public PostPageResponse getPosts(PostProperty property, Pageable pageable) {
        Page<Post> page = postRepository.findByProperty(property, pageable);

        return PostPageResponse.from(page);
    }
}