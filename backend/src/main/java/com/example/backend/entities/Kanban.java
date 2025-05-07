package com.example.backend.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "kanban")
public class Kanban {

    @Id
    private String id;
    private String description;
    private String status;

    public Kanban(String id, String description, String status) {
        this.id = id;
        this.description = description;
        this.status = status;
    }

    public Kanban() {
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}