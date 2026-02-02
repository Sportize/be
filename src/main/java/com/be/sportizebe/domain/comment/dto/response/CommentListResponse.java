package com.be.sportizebe.domain.comment.dto.response;

import java.util.List;

public record CommentListResponse(
        List<CommentResponse> comments
) {
    public static CommentListResponse of(List<CommentResponse> comments) {
        return new CommentListResponse(comments);
    }
}