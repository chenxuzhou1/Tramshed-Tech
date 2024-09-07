package com.example.demo.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class VenueService {

    @Value("${notion.api.url}")
    private String notionApiUrl;

    @Value("${notion.api.token}")
    private String notionApiToken;


    @Value("${notion.api.venue-database-id}")
    private String venueDatabaseId;

    @Value("${notion.api.version}")
    private String notionApiVersion;

    private final ObjectMapper objectMapper = new ObjectMapper();

    // 获取事件详情
    public JsonNode getAllVenues() throws IOException {
        String url = notionApiUrl + "/v1/databases/" + venueDatabaseId +"/query";
        HttpPost request = new HttpPost(url);
        request.addHeader("Authorization", "Bearer " + notionApiToken);
        request.addHeader("Content-Type", "application/json");
        request.addHeader("Notion-Version", "2022-06-28");

        try (CloseableHttpClient httpClient = HttpClients.createDefault();
             CloseableHttpResponse response = httpClient.execute(request)) {
            return objectMapper.readTree(response.getEntity().getContent());
        }
    }



    // 获取场地详情
    public JsonNode getVenueById(String venueId) throws IOException {
        String url = notionApiUrl + "/v1/pages/" + venueId;
        return performNotionApiRequest(url);
    }

    private JsonNode performNotionApiRequest(String url) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet request = new HttpGet(url);
        request.setHeader("Authorization", "Bearer " + notionApiToken);
        request.setHeader("Content-Type", "application/json");
        request.setHeader("Notion-Version", notionApiVersion);

        try (CloseableHttpResponse response = httpClient.execute(request)) {
            if (response.getStatusLine().getStatusCode() == HttpStatus.OK.value()) {
                return objectMapper.readTree(response.getEntity().getContent());
            } else {
                throw new RuntimeException("Failed to communicate with Notion API: " + response.getStatusLine());
            }
        }
    }
}
