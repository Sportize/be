package com.be.sportizebe.domain.post.controller;

import com.be.sportizebe.domain.post.dto.request.CreatePostRequest;
import com.be.sportizebe.domain.post.dto.response.CreatePostResponse;
import com.be.sportizebe.domain.post.entity.PostProperty;
import com.be.sportizebe.domain.post.service.PostService;
import com.be.sportizebe.global.response.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name = "post", description = "게시글 관련 API")
public class PostController {

    private final PostService postService;

    /**
     * Create a post for the specified board type.
     *
     * @param property the board type for the new post (e.g., SOCCER, BASKETBALL, FREE)
     * @param request  the request body containing the new post's details
     * @return a ResponseEntity with HTTP 201 containing a BaseResponse that wraps the created post data and a success message
     */
    @PostMapping("/posts/{property}")
    @Operation(summary = "게시글 생성", description = "게시판 종류별 게시글 생성")
    public ResponseEntity<BaseResponse<CreatePostResponse>> createPost(
        @Parameter(description = "게시판 종류 (SOCCER, BASKETBALL, FREE)") @PathVariable PostProperty property,
        @RequestBody @Valid CreatePostRequest request) {
        CreatePostResponse response = postService.createPost(property, request);
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(BaseResponse.success("게시글 생성 성공", response));
    }
}