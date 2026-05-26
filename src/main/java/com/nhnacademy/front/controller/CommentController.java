package com.nhnacademy.front.controller;

import com.nhnacademy.front.client.CommentClient;
import com.nhnacademy.front.dto.comment.CommentCreateRequest;
import com.nhnacademy.front.dto.comment.CommentUpdateRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/projects/{project-id}/tasks/{task-id}/comments")
public class CommentController {

    private final CommentClient commentClient;

    // 댓글 생성
    @PostMapping
    public String createComment(
            @PathVariable("project-id") Long projectId,
            @PathVariable("task-id") Long taskId,
            @ModelAttribute CommentCreateRequest req
    ) {
        commentClient.createComment(projectId, taskId, req);

        return "redirect:/projects/{project-id}/tasks/{task-id}";
    }

    // 댓글 수정
    @PostMapping("/{comment-id}")
    public String updateComment(
            @PathVariable("project-id") Long projectId,
            @PathVariable("task-id") Long taskId,
            @PathVariable("comment-id") Long commentId,
            @ModelAttribute CommentUpdateRequest req
    ) {
        commentClient.updateComment(projectId, taskId, commentId, req);

        return "redirect:/projects/{project-id}/tasks/{task-id}";
    }

    // 댓글 삭제
    @DeleteMapping("/{comment-id}")
    public String deleteComment(
            @PathVariable("project-id") Long projectId,
            @PathVariable("task-id") Long taskId,
            @PathVariable("comment-id") Long commentId
    ) {
        commentClient.deleteComment(projectId, taskId, commentId);

        return "redirect:/projects/{project-id}/tasks/{task-id}";
    }
}
