package com.example.backend.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "timeblocks")
public class TimeBlock {

    @Id
    private String id;
    private String description;
    private String startTime;
    private String endTime;

    public TimeBlock(String id, String description, String startTime, String endTime) {
        this.id = id;
        this.description = description;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public TimeBlock() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}
