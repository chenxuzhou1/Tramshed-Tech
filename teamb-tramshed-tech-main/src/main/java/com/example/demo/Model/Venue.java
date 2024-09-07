package com.example.demo.Model;

public class Venue {

    private String venueName;
    private String location;
    private String status;
    private int maxCapacity;
    private boolean reservationStatus;
    private int price;
    private String requirement;
    private String event;

    // Constructors, Getters, and Setters
    public Venue() {
    }

    public String getVenueName() {
        return venueName;
    }

    public void setVenueName(String venueName) {
        this.venueName = venueName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public void setMaxCapacity(int maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    public boolean isReservationStatus() {
        return reservationStatus;
    }

    public void setReservationStatus(boolean reservationStatus) {
        this.reservationStatus = reservationStatus;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getRequirement() {
        return requirement;
    }

    public void setRequirement(String requirement) {
        this.requirement = requirement;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    @Override
    public String toString() {
        return "Venue{" +
                "venueName='" + venueName + '\'' +
                ", location='" + location + '\'' +
                ", status='" + status + '\'' +
                ", maxCapacity=" + maxCapacity +
                ", reservationStatus=" + reservationStatus +
                ", price=" + price +
                ", requirement='" + requirement + '\'' +
                ", event='" + event + '\'' +
                '}';
    }
}
