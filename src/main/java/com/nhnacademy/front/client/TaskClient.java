package com.nhnacademy.front.client;

import com.nhnacademy.front.dto.task.TaskCreateRequest;
import com.nhnacademy.front.dto.task.TaskDetailResponse;
import com.nhnacademy.front.dto.task.TaskSummaryResponse;
import com.nhnacademy.front.dto.task.TaskUpdateRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class TaskClient {

    private final RestTemplate restTemplate;

    @Value("${api.task.url}")
    private String taskUrl;

    // 테스크 리스트 조회
    public List<TaskSummaryResponse> getTasks(Long projectId) {
        return restTemplate.getForObject(taskUrl + "/projects/" + projectId + "/tasks", List.class);
    }

    // 테스크 생성
    public TaskDetailResponse createTask(Long projectId, TaskCreateRequest req) {
        return restTemplate.postForObject(taskUrl+"/projects/"+projectId+"/tasks", req, TaskDetailResponse.class);
    }

    // 테스크 단건 조회
    public TaskDetailResponse getTask(Long projectId, Long taskId) {
        return restTemplate.getForObject(taskUrl + "/projects/" + projectId + "/tasks/" + taskId, TaskDetailResponse.class);
    }

    // 테스크 수정
    public void updateTask(Long projectId, Long taskId, TaskUpdateRequest req) {
        restTemplate.postForObject(taskUrl+"/projects/"+projectId+"/tasks/"+taskId, req, TaskDetailResponse.class);
    }

    // 테스크 삭제
    public void deleteTask(Long projectId, Long taskId) {
        restTemplate.delete(taskUrl + "/projects/" + projectId + "/tasks/" + taskId);
    }
}
