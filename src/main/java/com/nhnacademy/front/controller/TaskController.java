package com.nhnacademy.front.controller;

import com.nhnacademy.front.client.CommentClient;
import com.nhnacademy.front.client.MilestoneClient;
import com.nhnacademy.front.client.TagClient;
import com.nhnacademy.front.client.TaskClient;
import com.nhnacademy.front.dto.comment.CommentResponse;
import com.nhnacademy.front.dto.milestone.MilestoneResponse;
import com.nhnacademy.front.dto.tag.TagResponse;
import com.nhnacademy.front.dto.task.TaskCreateRequest;
import com.nhnacademy.front.dto.task.TaskDetailResponse;
import com.nhnacademy.front.dto.task.TaskUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/projects/{project-id}/tasks")
public class TaskController {

    private final TaskClient taskClient;
    private final MilestoneClient milestoneClient;
    private final TagClient tagClient;
    private final CommentClient commentClient;

    // 테스크 생성
    @PostMapping
    public String createTask(
            @PathVariable("project-id") Long projectId,
            @ModelAttribute TaskCreateRequest req
            ) {
        taskClient.createTask(projectId, req);

        return "redirect:/projects/{project-id}";
    }

    // 테스크 단건 조회
    @GetMapping("/{task-id}")
    public String getTask(
            @PathVariable("project-id") Long projectId,
            @PathVariable("task-id") Long taskId,
            Model model
    ) {
        TaskDetailResponse resp = taskClient.getTask(projectId, taskId);
        model.addAttribute("task", resp);

        List<MilestoneResponse> mileResp=milestoneClient.getMilestones(projectId);
        model.addAttribute("milestones", mileResp);

        List<TagResponse> tagResp=tagClient.getTags(projectId);
        model.addAttribute("tags", tagResp);

        List<CommentResponse> commentResp=commentClient.getComments(projectId, taskId);
        model.addAttribute("comments", commentResp);

        return "task/detail";
    }

    // 테스크 수정
    @PostMapping("/{task-id}")
    public String updateTask(
            @PathVariable("project-id") Long projectId,
            @PathVariable("task-id") Long taskId,
            @ModelAttribute TaskUpdateRequest req
            ) {
        taskClient.updateTask(projectId, taskId, req);

        return "redirect:/projects/{project-id}/tasks/{task-id}";
    }

    // 테스크 삭제
    @DeleteMapping("/{task-id}")
    public String deleteTask(
            @PathVariable("project-id") Long projectId,
            @PathVariable("task-id") Long taskId
    ) {
        taskClient.deleteTask(projectId, taskId);

        return "redirect:/projects/{project-id}";
    }
}
