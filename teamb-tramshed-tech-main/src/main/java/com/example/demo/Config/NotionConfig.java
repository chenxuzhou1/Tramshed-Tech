package com.example.demo.Config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "notion.api")
public class NotionConfig {
    private String url;
    private String token;
    private String version;
    private String eventDatabaseId;

    // Getters and Setters
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getEventDatabaseId() {
        return eventDatabaseId;
    }

    public void setEventDatabaseId(String eventDatabaseId) {
        this.eventDatabaseId = eventDatabaseId;
    }
}
