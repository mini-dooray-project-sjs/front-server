package com.nhnacademy.front.dto.task;

import lombok.Builder;

import java.util.List;

@Builder
public record TaskCreateRequest(
        String title,
        String content,
        Long projectId,
        Long milestoneId,
        List<Long> tagIds
) {
}
