package com.nhnacademy.front.dto.tag;

import lombok.Builder;

@Builder
public record TagResponse(
        Long tagId,
        String name
) {
}
