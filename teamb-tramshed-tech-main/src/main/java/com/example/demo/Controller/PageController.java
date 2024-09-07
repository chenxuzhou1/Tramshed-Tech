package com.example.demo.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {
    
    @GetMapping("/")
    public String home() {
        return "Introduction";
    }
    
    @GetMapping("/admin")
    public String admin() {
        return "admin";
    }

    @GetMapping("/eventForm")
    public String showForm(Model model) {
        return "eventForm";
    }

    @GetMapping("/Introduction")
    public String introduction(){
        return "Introduction";
    }

    @GetMapping("/Login")
    public String login() {
        return "Login";
    }

    @GetMapping("/Registration")
    public String Registration() {
        return "Registration";
    }

    @GetMapping("/EventSpace")
    public String showForm() {
        return "OrganiserInterface";
    }
    @GetMapping("/EditProfile")
    public String showFile() {
        return "Profile";
    }
}