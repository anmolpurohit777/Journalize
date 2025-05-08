package com.example.backend.services;

import com.example.backend.entities.CountdownGoal;
import com.example.backend.entities.User;
import com.example.backend.repositories.CountdownGoalRepository;
import com.example.backend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CountdownGoalService {

    @Autowired
    private CountdownGoalRepository countdownGoalRepository;
    @Autowired
    private UserRepository userRepository;

    public CountdownGoal createGoal(String userId, CountdownGoal goal) {
        CountdownGoal savedGoal = countdownGoalRepository.save(goal);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.getCountdownGoals().add(savedGoal);
        userRepository.save(user);

        return savedGoal;
    }

    public List<CountdownGoal> getGoalsForUser(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return user.getCountdownGoals();
    }

    public void deleteGoal(String userId, String goalId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        CountdownGoal goal = countdownGoalRepository.findById(goalId)
                .orElseThrow(() -> new RuntimeException("Goal not found"));

        user.getCountdownGoals().removeIf(g -> g.getId().equals(goalId));
        userRepository.save(user);

        countdownGoalRepository.deleteById(goalId);
    }
}

