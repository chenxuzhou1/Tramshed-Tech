package com.example.demo.Controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

@Controller
@RequestMapping("/api/proxy")
public class NotionProxyController {

    @Value("${notion.api.url}")
    private String notionApiUrl;

    @Value("${notion.api.token}")
    private String notionApiToken;

    @Value("${notion.api.venue-database-id}")
    private String venueDatabaseId;
    @Value("${notion.api.admin-database-id}")
    private String adminDatabaseId;

    @Value("${notion.api.version}")
    private String notionApiVersion;

    private final RestTemplate restTemplate = new RestTemplate();

    @PostMapping("/venues")
    public ResponseEntity<String> proxyVenuesRequest() {
        String url = notionApiUrl + "/v1/databases/" + venueDatabaseId + "/query";
        // 设置请求头
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + notionApiToken);
        headers.set("Notion-Version", notionApiVersion);
        headers.setContentType(MediaType.APPLICATION_JSON);

        // 创建请求实体
        HttpEntity<String> entity = new HttpEntity<>(headers);

        // 发送POST请求
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
        return ResponseEntity.ok().body(response.getBody());
    }
    @PostMapping("/admins")
    public ResponseEntity<String> proxyAdminsRequest() {
        String url = notionApiUrl + "/v1/databases/" + adminDatabaseId + "/query";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + notionApiToken);
        headers.set("Notion-Version", notionApiVersion);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
        return ResponseEntity.ok().body(response.getBody());
    }



}