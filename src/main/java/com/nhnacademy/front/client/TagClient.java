package com.nhnacademy.front.client;

import com.nhnacademy.front.dto.tag.TagCreateRequest;
import com.nhnacademy.front.dto.tag.TagResponse;
import com.nhnacademy.front.dto.tag.TagUpdateRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class TagClient {

    private final RestTemplate restTemplate;

    @Value("${api.task.url}")
    private String taskUrl;

    // 프로젝트의 태그 목록 조회
    public List<TagResponse> getTags(Long projectId) {
        return restTemplate.getForObject(taskUrl + "/projects/" + projectId + "/tags", List.class);
    }

    // 태그 생성
    public void createTag(Long projectId, TagCreateRequest req) {
        restTemplate.postForObject(taskUrl + "/projects/" + projectId + "/tags", req, TagResponse.class);
    }

    // 태그 업데이트
    public void updateTag(Long projectId, Long tagId, TagUpdateRequest req) {
        restTemplate.postForObject(taskUrl + "/projects/" + projectId + "/tags/" + tagId, req, TagResponse.class);
    }

    // 태그 삭제
    public void deleteTag(Long projectId, Long tagId) {
        restTemplate.delete(taskUrl + "/projects/" + projectId + "/tags/" + tagId);
    }
}
