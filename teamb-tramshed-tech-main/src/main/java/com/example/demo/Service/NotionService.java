package com.example.demo.Service;

import com.example.demo.Config.NotionConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class NotionService {

    @Autowired
    private NotionConfig notionConfig;

    public ResponseEntity<String> createEvent(Map<String, Object> eventDetails) {
        RestTemplate restTemplate = new RestTemplate();
        String url = notionConfig.getUrl() + "/v1/pages";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(notionConfig.getToken());
        headers.set("Notion-Version", notionConfig.getVersion());

        Map<String, Object> properties = new HashMap<>();
        properties.put("Event Name", Map.of("title", new Object[]{Map.of("text", Map.of("content", eventDetails.get("eventName")))}));
        properties.put("Event Type", Map.of("select", Map.of("name", eventDetails.get("eventType"))));
        properties.put("Date", Map.of("date", Map.of("start", eventDetails.get("eventStartDate"), "end", eventDetails.get("eventEndDate"))));
        properties.put("Status", Map.of("status", Map.of("name", eventDetails.get("eventStatus"))));
        properties.put("The number of participants", Map.of("number", eventDetails.get("eventParticipants")));
        properties.put("Venue", Map.of("relation", new Object[]{Map.of("id", eventDetails.get("eventVenue"))}));
        properties.put("Admin", Map.of("relation", new Object[]{Map.of("id", eventDetails.get("eventAdmin"))}));
        properties.put("Client", Map.of("relation", new Object[]{Map.of("id", eventDetails.get("eventClient"))}));

        // Handling multi_select fields correctly
        properties.put("Equipment Requirement", Map.of("multi_select", mapMultiSelect((String) eventDetails.get("eventEquipment"))));
        properties.put("Food Requirement", Map.of("multi_select", mapMultiSelect((String) eventDetails.get("eventFood"))));

        properties.put("Cancelled", Map.of("checkbox", eventDetails.get("eventCancelled")));

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("parent", Map.of("database_id", notionConfig.getEventDatabaseId()));
        requestBody.put("properties", properties);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        return restTemplate.postForEntity(url, entity, String.class);
    }

    private Object[] mapMultiSelect(String multiSelectString) {
        String[] items = multiSelectString.split(",");
        Object[] multiSelect = new Object[items.length];
        for (int i = 0; i < items.length; i++) {
            multiSelect[i] = Map.of("name", items[i].trim());
        }
        return multiSelect;
    }
}
