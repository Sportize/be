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
import com.be.sportizebe.global.s3.enums.PathName;
import com.be.sportizebe.global.s3.service.S3Service;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final S3Service s3Service;

    @Override
    @CacheEvict(cacheNames = "postList", allEntries = true)
    @Transactional
    public PostResponse createPost(PostProperty property, CreatePostRequest request, MultipartFile image, User user) {
        // 이미지가 있으면 S3에 업로드
        String imgUrl = null;
        if (image != null && !image.isEmpty()) {
            imgUrl = s3Service.uploadFile(PathName.POST, image);
        }

        Post post = request.toEntity(property, user, imgUrl);
        Post savedPost = postRepository.save(post);

        return PostResponse.from(savedPost);
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

        // 여기서도 imgUrl 업데이트가 request에 포함이면 그대로 반영
        post.update(request.title(), request.content(), request.imgUrl());

        return PostResponse.from(post);
    }

    @Override
    @Cacheable(cacheNames = "postList", keyGenerator = "postListKeyGenerator")
    public PostPageResponse getPosts(PostProperty property, Pageable pageable) {
        Page<Post> page = postRepository.findByProperty(property, pageable);
        return PostPageResponse.from(page);
    }
}