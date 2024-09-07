package com.example.demo.Controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.example.demo.Service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admins")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @GetMapping("/pp")
    public ResponseEntity<JsonNode> getAllAdmins() {
        try {
            JsonNode admins = adminService.getAllAdmins();
            return ResponseEntity.ok(admins);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
