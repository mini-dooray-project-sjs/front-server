package com.nhnacademy.front.dto.task;

import com.nhnacademy.front.dto.milestone.MilestoneResponse;
import com.nhnacademy.front.dto.tag.TagResponse;
import lombok.Builder;

import java.util.List;

@Builder
public record TaskDetailResponse(
        Long taskId,
        Long projectId,
        String title,
        String content,
        MilestoneResponse milestone,
        List<TagResponse> tags
) {
}
