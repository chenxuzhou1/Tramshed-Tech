package com.example.demo.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;


import java.io.IOException;

@Service
public class EventService {

    @Autowired
    private VenueService venueService;
    @Autowired
    private ClientService clientService;
    @Autowired
    private RestTemplate restTemplate;

    @Value("${notion.api.url}")
    private String notionApiUrl;

    @Value("${notion.api.token}")
    private String notionApiToken;

    @Value("${notion.api.event-database-id}")
    private String databaseId;
    @Value("${notion.api.version}")
    private String notionVersion;
    private final ObjectMapper objectMapper = new ObjectMapper();

    // 获取所有事件
    public JsonNode getAllEvents() throws IOException {
        String url = notionApiUrl + "/v1/databases/" + databaseId + "/query";
        HttpPost request = new HttpPost(url);
        request.addHeader("Authorization", "Bearer " + notionApiToken);
        request.addHeader("Content-Type", "application/json");
        request.addHeader("Notion-Version", "2022-06-28");

        try (CloseableHttpClient httpClient = HttpClients.createDefault();
             CloseableHttpResponse response = httpClient.execute(request)) {
            return objectMapper.readTree(response.getEntity().getContent());
        }
    }

    // 根据ID获取事件
    public JsonNode getEventById(String eventId) throws IOException {
        String url = notionApiUrl + "/v1/pages/" + eventId;
        HttpGet request = new HttpGet(url);
        request.addHeader("Authorization", "Bearer " + notionApiToken);
        request.addHeader("Content-Type", "application/json");
        request.addHeader("Notion-Version", "2022-06-28");

        try (CloseableHttpClient httpClient = HttpClients.createDefault();
             CloseableHttpResponse response = httpClient.execute(request)) {
            return objectMapper.readTree(response.getEntity().getContent());
        }
    }

    // 创建或更新事件
    public JsonNode updateEvent(String eventId, JsonNode eventJson) throws IOException {
        String url = notionApiUrl + "/v1/pages/" + eventId;
        HttpPatch patch = new HttpPatch(url);
        patch.setHeader("Authorization", "Bearer " + notionApiToken);
        patch.setHeader("Content-Type", "application/json");
        patch.setHeader("Notion-Version", notionVersion);

        StringEntity entity = new StringEntity(eventJson.toString());
        patch.setEntity(entity);

        try (CloseableHttpClient httpClient = HttpClients.createDefault();
             CloseableHttpResponse response = httpClient.execute(patch)) {
            if (response.getStatusLine().getStatusCode() == HttpStatus.OK.value()) {
                return objectMapper.readTree(response.getEntity().getContent());
            } else {
                throw new RuntimeException("Failed to update the event in Notion: " + response.getStatusLine());
            }
        }
    }

    // 删除事件 - Notion API does not support deleting pages via API directly
    public JsonNode cancelEvent(String eventId) throws IOException {
        String url = notionApiUrl + "/v1/pages/" + eventId;
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPatch patch = new HttpPatch(url);
        patch.setHeader("Authorization", "Bearer " + notionApiToken);
        patch.setHeader("Content-Type", "application/json");
        patch.setHeader("Notion-Version", notionVersion);

        // 构建取消事件的JSON请求体
        StringEntity entity = new StringEntity("{\"properties\": {\"Cancelled\": {\"checkbox\": true}}}");
        patch.setEntity(entity);

        try (CloseableHttpResponse response = httpClient.execute(patch)) {
            if (response.getStatusLine().getStatusCode() == HttpStatus.OK.value()) {
                return objectMapper.readTree(response.getEntity().getContent());
            } else {
                throw new RuntimeException("Failed to cancel the event in Notion: " + response.getStatusLine());
            }
        }
    }
    public JsonNode getEventDetailsWithVenue(String eventId) throws IOException {
        JsonNode event = getEventById(eventId);  // 获取事件基础信息
        if (event.has("properties") && event.get("properties").has("Venue") && !event.get("properties").get("Venue").get("relation").isEmpty()) {
            String venueId = event.get("properties").get("Venue").get("relation").get(0).get("id").asText();
            JsonNode venueDetails = venueService.getVenueById(venueId);  // 获取场地详细信息
            ((ObjectNode) event).set("venueDetails", venueDetails);  // 添加场地信息到事件对象
        }
        return event;
    }


    private String buildEventJson(JsonNode event) {
        // 从 JsonNode 提取数据
        String eventName = event.path("eventName").asText(); // 使用默认值""如果节点不存在
        String eventDate = event.path("date").asText();
        String eventStatus = event.path("status").asText();

        // 构建 JSON 字符串
        return String.format(
                "{ \"properties\": { " +
                        "\"Event Name\": { \"title\": [{ \"text\": { \"content\": \"%s\" }}]}, " +
                        "\"Date\": { \"date\": { \"start\": \"%s\" }}, " +
                        "\"Status\": { \"select\": { \"name\": \"%s\" }} " +
                        "} }",
                eventName, eventDate, eventStatus
        );
    }

    public JsonNode getEventsByUserEmail(String userEmail) throws IOException {
        // First, get the client ID from the eail
        String clientId = clientService.getClientIdByEmail(userEmail);

        String url = notionApiUrl + "/v1/databases/" + databaseId + "/query";
        HttpPost request = new HttpPost(url);
        request.setHeader("Authorization", "Bearer " + notionApiToken);
        request.setHeader("Content-Type", "application/json");
        request.setHeader("Notion-Version", notionVersion);

        // Construct the JSON body to filter events by client ID
        String filterJson = String.format(
                "{\"filter\": {\"property\": \"Client\", \"relation\": {\"contains\": \"%s\"}}}",
                clientId
        );
        StringEntity entity = new StringEntity(filterJson);
        request.setEntity(entity);

        try (CloseableHttpClient httpClient = HttpClients.createDefault();
             CloseableHttpResponse response = httpClient.execute(request)) {
            JsonNode jsonResponse = objectMapper.readTree(response.getEntity().getContent());
            if (jsonResponse.has("results")) {
                return jsonResponse.get("results");
            } else {
                throw new IOException("Failed to fetch events for the client with ID " + clientId);
            }
        }
    }
}
