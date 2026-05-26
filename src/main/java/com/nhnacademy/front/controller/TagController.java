package com.nhnacademy.front.controller;

import com.nhnacademy.front.client.TagClient;
import com.nhnacademy.front.dto.tag.TagCreateRequest;
import com.nhnacademy.front.dto.tag.TagUpdateRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/projects/{project-id}/tags")
public class TagController {

    private final TagClient tagClient;

    // 태그 생성
    @PostMapping
    public String createTag(
            @PathVariable("project-id") Long projectId,
            @ModelAttribute TagCreateRequest req
    ) {
        tagClient.createTag(projectId, req);

        return "redirect:/projects/" + projectId;
    }

    // 태그 업데이트
    @PostMapping("/{tag-id}")
    public String updateTag(
            @PathVariable("project-id") Long projectId,
            @PathVariable("tag-id") Long tagId,
            @ModelAttribute TagUpdateRequest req
    ) {
        tagClient.updateTag(projectId, tagId, req);

        return "redirect:/projects/" + projectId;
    }

    // 태그 삭제
    @DeleteMapping("/{tag-id}")
    public String deleteTag(
            @PathVariable("project-id") Long projectId,
            @PathVariable("tag-id") Long tagId
    ) {
        tagClient.deleteTag(projectId, tagId);

        return "redirect:/projects/" + projectId;
    }
}
