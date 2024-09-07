package com.example.demo.Service;



import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class AdminService {

    @Value("${notion.api.url}")
    private String notionApiUrl;

    @Value("${notion.api.token}")
    private String notionApiToken;

    @Value("${notion.api.admin-database-id}")
    private String adminDatabaseId;

    @Value("${notion.api.version}")
    private String notionApiVersion;

    private final ObjectMapper objectMapper = new ObjectMapper();

    // Fetch all admins from Notion
    public JsonNode getAllAdmins() throws IOException {
        String url = notionApiUrl + "/v1/databases/" + adminDatabaseId + "/query";
        HttpPost request = new HttpPost(url);
        request.addHeader("Authorization", "Bearer " + notionApiToken);
        request.addHeader("Content-Type", "application/json");
        request.addHeader("Notion-Version", notionApiVersion);

        try (CloseableHttpClient httpClient = HttpClients.createDefault();
             CloseableHttpResponse response = httpClient.execute(request)) {
            if (response.getStatusLine().getStatusCode() == HttpStatus.OK.value()) {
                return objectMapper.readTree(response.getEntity().getContent());
            } else {
                throw new RuntimeException("Failed to communicate with Notion API: " + response.getStatusLine());
            }
        }
    }
}
