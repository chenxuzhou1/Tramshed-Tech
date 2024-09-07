package com.example.demo.Controller;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.demo.Service.ClientService;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@RestController
@RequestMapping("/clients")
public class ClientController {

    @Autowired
    private ClientService clientService;

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody Map<String, String> credentials, HttpServletResponse response) {
        String email = credentials.get("email");
        String password = credentials.get("password");
        try {
            boolean isAuthenticated = clientService.authenticateClient(email, password);
            if (isAuthenticated) {
                JsonNode userDetails = clientService.getClientDetails(email);
                if (userDetails != null) {
                    String userName = userDetails.get("properties").get("Full Name").get("title").get(0).get("plain_text").asText();
                    String encodedUserName = URLEncoder.encode(userName, StandardCharsets.UTF_8.toString());

                    Cookie emailCookie = new Cookie("userEmail", email);
                    emailCookie.setPath("/");
                    emailCookie.setMaxAge(24 * 60 * 60); // 设置 cookie 有效期为一天
                    response.addCookie(emailCookie);

                    Cookie nameCookie = new Cookie("userName", encodedUserName);
                    nameCookie.setPath("/");
                    nameCookie.setMaxAge(24 * 60 * 60);
                    response.addCookie(nameCookie);

                    return ResponseEntity.ok(userName);
                } else {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User details not found");
                }
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
            }
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Server error: " + e.getMessage());
        }
    }
    }

