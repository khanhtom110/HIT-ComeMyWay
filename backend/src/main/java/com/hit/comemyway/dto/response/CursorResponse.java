package com.hit.comemyway.dto.response;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Slice;

import java.util.List;

@Getter
@Builder
public class CursorResponse<T> {
  //@formatter:off
    private List<T> content;
    private boolean hasNext;
    private Long lastPostId;

    public static <T> CursorResponse<T> of(Slice<T> slice, Long lastPostId) {
        return CursorResponse.<T>builder()
                .content(slice.getContent())
                .hasNext(slice.hasNext())
                .lastPostId(lastPostId)
                .build();
    }
}