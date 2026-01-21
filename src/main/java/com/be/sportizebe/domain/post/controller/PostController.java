package com.be.sportizebe.domain.post.controller;

import com.be.sportizebe.domain.post.dto.request.CreatePostRequest;
import com.be.sportizebe.domain.post.dto.request.UpdatePostRequest;
import com.be.sportizebe.domain.post.dto.response.PostResponse;
import com.be.sportizebe.domain.post.entity.PostProperty;
import com.be.sportizebe.domain.post.service.PostService;
import com.be.sportizebe.domain.user.entity.User;
import com.be.sportizebe.domain.user.repository.UserRepository;
import com.be.sportizebe.global.response.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name = "post", description = "게시글 관련 API")
public class PostController {

    private final PostService postService;
    private final UserRepository userRepository; // TODO: 인증 로직 개발 후 제거

    @PostMapping("/posts/{property}")
    @Operation(summary = "게시글 생성", description = "게시판 종류별 게시글 생성")
    public ResponseEntity<BaseResponse<PostResponse>> createPost(
        @Parameter(description = "게시판 종류 (SOCCER, BASKETBALL, FREE)") @PathVariable PostProperty property,
        @RequestBody @Valid CreatePostRequest request) {
        PostResponse response = postService.createPost(property, request);
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(BaseResponse.success("게시글 생성 성공", response));
    }

    @PutMapping("/posts/{postId}")
    @Operation(summary = "게시글 수정", description = "게시글 수정 (작성자만 가능)")
    public ResponseEntity<BaseResponse<PostResponse>> updatePost(
        @Parameter(description = "게시글 ID") @PathVariable Long postId,
        @RequestBody @Valid UpdatePostRequest request) {
        // TODO: 인증 로직 개발 후 @AuthenticationPrincipal User user로 변경
        User user = userRepository.findById(1L)
            .orElseThrow(() -> new RuntimeException("테스트 유저가 없습니다. users 테이블에 id=1인 유저를 추가해주세요."));
        PostResponse response = postService.updatePost(postId, request, user);
        return ResponseEntity.ok(BaseResponse.success("게시글 수정 성공", response));
    }

    @DeleteMapping("/posts/{postId}")
    @Operation(summary = "게시글 삭제", description = "게시글 삭제 (작성자만 가능)")
    public ResponseEntity<BaseResponse<Void>> deletePost(
        @Parameter(description = "게시글 ID") @PathVariable Long postId) {
        // TODO: 인증 로직 개발 후 @AuthenticationPrincipal User user로 변경
        User user = userRepository.findById(1L)
            .orElseThrow(() -> new RuntimeException("테스트 유저가 없습니다. users 테이블에 id=1인 유저를 추가해주세요."));
        postService.deletePost(postId, user);
        return ResponseEntity.ok(BaseResponse.success("게시글 삭제 성공", null));
    }
}
