package com.be.sportizebe.domain.chat.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Builder
public class CursorPageResponse<T>{
    private List<T> items;      // 이번에 내려준 메시지들
    private Long nextCursor;    // 다음 요청에서 beforeId로 보낼 값
    private boolean hasNext;    // 더 과거 메시지가 있는지
}
