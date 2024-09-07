package com.example.demo.Controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.example.demo.Service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;

@RestController
@RequestMapping("/events")
public class EventApiController {

    @Autowired
    private EventService eventService;

    // 获取所有事件
    @GetMapping("/pp")
    public ResponseEntity<JsonNode> getAllEvents() {
        try {
            JsonNode events = eventService.getAllEvents();

            return ResponseEntity.ok(events);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // 根据ID获取事件
    @GetMapping("/{eventId}")
    public ResponseEntity<JsonNode> getEventById(@PathVariable String eventId) {
        try {
            JsonNode event = eventService.getEventById(eventId);
            if (event != null && event.isObject()) {
                return ResponseEntity.ok(event);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // 创建或更新事件
    @PatchMapping("/{eventId}")
    public ResponseEntity<JsonNode> updateEvent(@PathVariable String eventId, @RequestBody JsonNode eventJson) {
        try {
            JsonNode updatedEvent = eventService.updateEvent(eventId, eventJson);
            return ResponseEntity.ok(updatedEvent);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    // 删除事件
    @PatchMapping("/{eventId}/cancel")
    public ResponseEntity<JsonNode> cancelEvent(@PathVariable String eventId) {
        try {
            JsonNode updatedEvent = eventService.cancelEvent(eventId);
            return ResponseEntity.ok(updatedEvent);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{eventId}/details")
    public ResponseEntity<JsonNode> getEventDetailsWithVenue(@PathVariable String eventId) {
        try {
            JsonNode eventDetails = eventService.getEventDetailsWithVenue(eventId);  // 调用修改后的服务方法
            return ResponseEntity.ok(eventDetails);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }


    @GetMapping("/myEvents")
    public ResponseEntity<?> getMyEvents(@CookieValue("userEmail") String userEmail) {
        try {
            JsonNode userEvents = eventService.getEventsByUserEmail(userEmail);
            System.out.println(userEvents);
            if (userEvents != null && userEvents.size() > 0) {
                return ResponseEntity.ok(userEvents);
            } else {
                return ResponseEntity.noContent().build();  // No events found for the user
            }
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error retrieving events: " + e.getMessage());
        }
    }

}
