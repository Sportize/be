package com.be.sportizebe.domain.post.controller;

import com.be.sportizebe.domain.post.dto.request.CreatePostRequest;
import com.be.sportizebe.domain.post.dto.request.UpdatePostRequest;
import com.be.sportizebe.domain.post.dto.response.PostPageResponse;
import com.be.sportizebe.domain.post.dto.response.PostResponse;
import com.be.sportizebe.domain.post.entity.PostProperty;
import com.be.sportizebe.domain.post.service.PostService;
import com.be.sportizebe.domain.user.entity.User;
import com.be.sportizebe.global.response.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name = "post", description = "게시글 관련 API")
public class PostController {

    private final PostService postService;

    @PostMapping("/posts/{property}")
    @Operation(summary = "게시글 생성", description = "게시판 종류별 게시글 생성")
    public ResponseEntity<BaseResponse<PostResponse>> createPost(
        @Parameter(description = "게시판 종류 (SOCCER, BASKETBALL, FREE)") @PathVariable PostProperty property,
        @RequestBody @Valid CreatePostRequest request,
        @AuthenticationPrincipal User user) {
        PostResponse response = postService.createPost(property, request, user);
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(BaseResponse.success("게시글 생성 성공", response));
    }

    @PutMapping("/posts/{postId}")
    @Operation(summary = "게시글 수정", description = "게시글 수정 (작성자만 가능)")
    public ResponseEntity<BaseResponse<PostResponse>> updatePost(
        @Parameter(description = "게시글 ID") @PathVariable Long postId,
        @RequestBody @Valid UpdatePostRequest request,
        @AuthenticationPrincipal User user) {
        PostResponse response = postService.updatePost(postId, request, user);
        return ResponseEntity.ok(BaseResponse.success("게시글 수정 성공", response));
    }

    @DeleteMapping("/posts/{postId}")
    @Operation(summary = "게시글 삭제", description = "게시글 삭제 (작성자만 가능)")
    public ResponseEntity<BaseResponse<Void>> deletePost(
        @Parameter(description = "게시글 ID") @PathVariable Long postId,
        @AuthenticationPrincipal User user) {
        postService.deletePost(postId, user);
        return ResponseEntity.ok(BaseResponse.success("게시글 삭제 성공", null));
    }

    @GetMapping("/posts/{property}")
    @Operation(summary = "게시글 목록 조회", description = "게시판 종류별 게시글 목록을 페이징하여 조회합니다.")
    public ResponseEntity<BaseResponse<PostPageResponse>> getPosts(
        @Parameter(description = "게시판 종류 (SOCCER, BASKETBALL, FREE)") @PathVariable PostProperty property,
        @Parameter(hidden = true) @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        PostPageResponse response = postService.getPosts(property, pageable);
        return ResponseEntity.ok(BaseResponse.success("게시글 목록 조회 성공", response));
    }
}
