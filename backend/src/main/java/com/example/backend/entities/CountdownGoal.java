package com.example.backend.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Document(collection = "countdown_goals")
public class CountdownGoal {
    @Id
    private String id;
    private String goal;
    private LocalDate endDate;

    public CountdownGoal(LocalDate endDate, String goal, String id) {
        this.endDate = endDate;
        this.goal = goal;
        this.id = id;
    }

    public CountdownGoal() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGoal() {
        return goal;
    }

    public void setGoal(String goal) {
        this.goal = goal;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
}
