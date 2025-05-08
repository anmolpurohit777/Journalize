package com.example.backend.controllers;

import com.example.backend.entities.CountdownGoal;
import com.example.backend.services.CountdownGoalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users/{userId}/countdown-goals")
public class CountdownGoalController {

    @Autowired
    private CountdownGoalService countdownGoalService;

    @PostMapping
    public ResponseEntity<CountdownGoal> createGoal(
            @PathVariable String userId,
            @RequestBody CountdownGoal goal) {
        CountdownGoal created = countdownGoalService.createGoal(userId, goal);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<CountdownGoal>> getGoals(@PathVariable String userId) {
        List<CountdownGoal> goals = countdownGoalService.getGoalsForUser(userId);
        return ResponseEntity.ok(goals);
    }

    @DeleteMapping("/{goalId}")
    public ResponseEntity<Void> deleteGoal(
            @PathVariable String userId,
            @PathVariable String goalId) {
        countdownGoalService.deleteGoal(userId, goalId);
        return ResponseEntity.noContent().build();
    }
}

