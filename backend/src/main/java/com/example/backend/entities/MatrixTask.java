package com.example.backend.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "matrix_tasks")
public class MatrixTask {

    @Id
    private String id;

    private String description;
    private Integer quadrant;

    public MatrixTask(String id, Integer quadrant, String description) {
        this.id = id;
        this.quadrant = quadrant;
        this.description = description;
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
}
