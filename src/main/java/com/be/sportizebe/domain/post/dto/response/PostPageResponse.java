package com.be.sportizebe.domain.post.dto.response;

import com.be.sportizebe.domain.post.entity.Post;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.domain.Page;

import java.util.List;

@Schema(title = "PostPageResponse", description = "게시글 목록 + 페이지 정보 응답")
public record PostPageResponse(

        @Schema(description = "게시글 목록")
        List<PostResponse> content,

        @Schema(description = "페이지 정보")
        PageInfoResponse pageInfo

) {
    public static PostPageResponse from(Page<Post> page) {
        return new PostPageResponse(
                page.getContent().stream()
                        .map(PostResponse::from)
                        .toList(),
                new PageInfoResponse(
                        page.getNumber(),
                        page.getSize(),
                        page.getTotalElements(),
                        page.getTotalPages(),
                        page.hasNext()
                )
        );
    }
}