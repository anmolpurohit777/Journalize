package com.example.backend.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "matrix_tasks")
public class MatrixTask {

    @Id
    private String id;

    private String description;
    private Integer quadrant;
    private boolean completed = false;


    public MatrixTask(String id, String description, Integer quadrant, boolean completed) {
        this.id = id;
        this.description = description;
        this.quadrant = quadrant;
        this.completed = completed;
    }

    public MatrixTask() {
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

    public Integer getQuadrant() {
        return quadrant;
    }

    public void setQuadrant(Integer quadrant) {
        this.quadrant = quadrant;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
}
