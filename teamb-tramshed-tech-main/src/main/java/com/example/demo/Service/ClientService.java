package com.example.demo.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class ClientService {

    @Value("${notion.api.url}")
    private String notionApiUrl;

    @Value("${notion.api.token}")
    private String notionApiToken;

    @Value("${notion.api.client-database-id}")
    private String clientDatabaseId;

    @Value("${notion.api.version}")
    private String notionApiVersion;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public JsonNode getAllClients() throws IOException {
        String url = notionApiUrl + "/v1/databases/" + clientDatabaseId + "/query";
        HttpPost request = new HttpPost(url);
        request.addHeader("Authorization", "Bearer " + notionApiToken);
        request.addHeader("Content-Type", "application/json");
        request.addHeader("Notion-Version", "2022-06-28");

        try (CloseableHttpClient httpClient = HttpClients.createDefault();
             CloseableHttpResponse response = httpClient.execute(request)) {
            if (response.getStatusLine().getStatusCode() == 200) {
                return objectMapper.readTree(response.getEntity().getContent());
            } else {
                throw new RuntimeException("Failed : HTTP error code : " + response.getStatusLine().getStatusCode());
            }
        }
    }

    public boolean authenticateClient(String email, String password) throws IOException {
        String url = notionApiUrl + "/v1/databases/" + clientDatabaseId + "/query";
        HttpPost request = new HttpPost(url);
        request.addHeader("Authorization", "Bearer " + notionApiToken);
        request.addHeader("Content-Type", "application/json");
        request.addHeader("Notion-Version", notionApiVersion);
        System.out.println("gg");
        JSONObject filter = new JSONObject();
        filter.put("and", new JSONObject[]{
                new JSONObject().put("property", "Email").put("email", new JSONObject().put("equals", email)),
                new JSONObject().put("property", "Password").put("rich_text", new JSONObject().put("equals", password))
        });

        JSONObject requestBody = new JSONObject();
        requestBody.put("filter", filter);
        StringEntity entity = new StringEntity(requestBody.toString());
        request.setEntity(entity);

        try (CloseableHttpClient httpClient = HttpClients.createDefault();
             CloseableHttpResponse response = httpClient.execute(request)) {
            JsonNode jsonResponse = objectMapper.readTree(response.getEntity().getContent());
            return jsonResponse.get("results").size() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public JsonNode getClientDetails(String email) throws IOException {
        String url = notionApiUrl + "/v1/databases/" + clientDatabaseId + "/query";
        HttpPost request = new HttpPost(url);
        request.addHeader("Authorization", "Bearer " + notionApiToken);
        request.addHeader("Content-Type", "application/json");
        request.addHeader("Notion-Version", notionApiVersion);

        JSONObject filter = new JSONObject();
        filter.put("property", "Email").put("email", new JSONObject().put("equals", email));

        JSONObject requestBody = new JSONObject();
        requestBody.put("filter", filter);
        StringEntity entity = new StringEntity(requestBody.toString());
        request.setEntity(entity);

        try (CloseableHttpClient httpClient = HttpClients.createDefault();
             CloseableHttpResponse response = httpClient.execute(request)) {
            JsonNode jsonResponse = objectMapper.readTree(response.getEntity().getContent());
            if (jsonResponse.has("results") && jsonResponse.get("results").size() > 0) {
                return jsonResponse.get("results").get(0); // 返回第一个匹配用户的完整信息
            }
        }
        return null;
    }


    public String getClientIdByEmail(String userEmail) throws IOException {
        String url = notionApiUrl + "/v1/databases/" + clientDatabaseId + "/query";
        HttpPost request = new HttpPost(url);
        request.addHeader("Authorization", "Bearer " + notionApiToken);
        request.addHeader("Content-Type", "application/json");
        request.addHeader("Notion-Version", notionApiVersion);


        String requestBody = String.format(
                "{\"filter\": {\"property\": \"Email\", \"email\": {\"equals\": \"%s\"}}}", userEmail);
        request.setEntity(new StringEntity(requestBody));

        try (CloseableHttpClient httpClient = HttpClients.createDefault();
             CloseableHttpResponse response = httpClient.execute(request)) {
            if (response.getStatusLine().getStatusCode() == HttpStatus.OK.value()) {
                JsonNode responseJson = objectMapper.readTree(response.getEntity().getContent());
                if (responseJson.has("results") && !responseJson.get("results").isEmpty()) {
                    return responseJson.get("results").get(0).get("id").asText();
                } else {
                    throw new IOException("No client found with the email " + userEmail);
                }
            } else {
                throw new IOException("Failed to fetch client ID from Notion API: " + response.getStatusLine());
            }
        }
    }
}
