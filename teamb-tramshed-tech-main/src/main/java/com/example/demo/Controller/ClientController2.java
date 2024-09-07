package com.example.demo.Controller;

import com.example.demo.Service.RegistrationService;
import com.example.demo.dto.ClientDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ClientController2 {
    @Autowired
    private RegistrationService registrationService;

    @PostMapping("/clients/create")
    public ResponseEntity<?> createClient(@RequestBody ClientDto clientDto) {
        try {
            registrationService.createClient(clientDto);
            return ResponseEntity.ok("Client created successfully");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error creating client: " + e.getMessage());
        }
    }
}
