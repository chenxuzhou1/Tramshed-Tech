package com.example.demo.Model;

import java.time.LocalDateTime; // Assuming Date includes time in your Notion database

public class Event {

    private String eventName;
    private LocalDateTime date;
    private String venue;
    private String foodRequirement;
    private String eventType;
    private String client;
    private String equipmentRequirement;
    private String admin;
    private String status;

    // Constructors, Getters, and Setters
    public Event() {
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public String getFoodRequirement() {
        return foodRequirement;
    }

    public void setFoodRequirement(String foodRequirement) {
        this.foodRequirement = foodRequirement;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public String getEquipmentRequirement() {
        return equipmentRequirement;
    }

    public void setEquipmentRequirement(String equipmentRequirement) {
        this.equipmentRequirement = equipmentRequirement;
    }

    public String getAdmin() {
        return admin;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Event{" +
                "eventName='" + eventName + '\'' +
                ", date=" + date +
                ", venue='" + venue + '\'' +
                ", foodRequirement='" + foodRequirement + '\'' +
                ", eventType='" + eventType + '\'' +
                ", client='" + client + '\'' +
                ", equipmentRequirement='" + equipmentRequirement + '\'' +
                ", admin='" + admin + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
