package com.nhnacademy.front.client;

import com.nhnacademy.front.dto.comment.CommentCreateRequest;
import com.nhnacademy.front.dto.comment.CommentResponse;
import com.nhnacademy.front.dto.comment.CommentUpdateRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class CommentClient {

    private final RestTemplate restTemplate;

    @Value("${api.task.url}")
    private String taskUrl;

    // 테스크의 댓글 목록 조회
    public List<CommentResponse> getComments(Long projectId, Long taskId) {
        String url=taskUrl+"/projects/{project-id}/tasks/{task-id}/comments";
        CommentResponse[] response = restTemplate.getForObject(url, CommentResponse[].class, projectId, taskId);
        return List.of(response);
    }

    // 댓글 생성
    public void createComment(Long projectId, Long taskId, CommentCreateRequest req) {
        String url=taskUrl+"/projects/{project-id}/tasks/{task-id}/comments";
        restTemplate.postForObject(url, req, CommentResponse.class, projectId, taskId);
    }

    // 댓글 수정
    public void updateComment(Long projectId, Long taskId, Long commentId, CommentUpdateRequest req) {
        String url=taskUrl+"/projects/{project-id}/tasks/{task-id}/comments/{comment-id}";
        restTemplate.postForObject(url, req, CommentResponse.class, projectId, taskId, commentId);
    }

    // 댓글 삭제
    public void deleteComment(Long projectId, Long taskId, Long commentId) {
        String url=taskUrl+"/projects/{project-id}/tasks/{task-id}/comments/{comment-id}";
        restTemplate.delete(url, projectId, taskId, commentId);
    }
}
