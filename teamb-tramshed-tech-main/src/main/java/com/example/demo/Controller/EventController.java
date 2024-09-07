package com.example.demo.Controller;

import com.example.demo.Service.NotionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.Map;

@Controller
public class EventController {

    @Autowired
    private NotionService notionService;

    @PostMapping("/submit")
    public String submitForm(
            @RequestParam("eventName") String eventName,
            @RequestParam("eventType") String eventType,
            @RequestParam("eventStartDate") String eventStartDate,
            @RequestParam("eventEndDate") String eventEndDate,
            @RequestParam("eventStatus") String eventStatus,
            @RequestParam("eventParticipants") Integer eventParticipants,
            @RequestParam("eventVenue") String eventVenue,
            @RequestParam("eventAdmin") String eventAdmin,
            @RequestParam("eventClient") String eventClient,
            @RequestParam("eventEquipment") String eventEquipment,
            @RequestParam("eventFood") String eventFood,
            @RequestParam(value = "eventCancelled", required = false) String eventCancelled,
            Model model) {

        Map<String, Object> eventDetails = new HashMap<>();
        eventDetails.put("eventName", eventName);
        eventDetails.put("eventType", eventType);
        eventDetails.put("eventStartDate", eventStartDate);
        eventDetails.put("eventEndDate", eventEndDate);
        eventDetails.put("eventStatus", eventStatus);
        eventDetails.put("eventParticipants", eventParticipants);
        eventDetails.put("eventVenue", eventVenue);
        eventDetails.put("eventAdmin", eventAdmin);
        eventDetails.put("eventClient", eventClient);
        eventDetails.put("eventEquipment", eventEquipment);
        eventDetails.put("eventFood", eventFood);
        eventDetails.put("eventCancelled", eventCancelled != null);

        notionService.createEvent(eventDetails);

        model.addAttribute("message", "Event submitted successfully!");
        return "eventForm";
    }
}
