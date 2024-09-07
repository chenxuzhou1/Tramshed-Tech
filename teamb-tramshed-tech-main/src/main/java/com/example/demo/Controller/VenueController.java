package com.example.demo.Controller;



import com.fasterxml.jackson.databind.JsonNode;
import com.example.demo.Service.VenueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/venues")
public class VenueController {

    @Autowired
    private VenueService venueService;

    @GetMapping("/pp")
    public ResponseEntity<JsonNode> getAllVenues() {
        try {
            JsonNode venues = venueService.getAllVenues();
            return ResponseEntity.ok(venues);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @GetMapping("/{venueId}")
    public ResponseEntity<JsonNode> getVenueById(@PathVariable String venueId) {
        try {
            JsonNode venue = venueService.getVenueById(venueId);
            return ResponseEntity.ok(venue);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
