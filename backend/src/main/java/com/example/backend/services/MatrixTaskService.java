package com.example.backend.services;

import com.example.backend.entities.MatrixTask;
import com.example.backend.entities.User;
import com.example.backend.repositories.MatrixTaskRepository;
import com.example.backend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MatrixTaskService {

    @Autowired
    private MatrixTaskRepository matrixTaskRepository;

    @Autowired
    private UserRepository userRepository;

    public MatrixTask createMatrixTaskForUser(String userId, MatrixTask matrixTask) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (!userOpt.isPresent()) {
            throw new RuntimeException("User not found");
        }
        User user = userOpt.get();
        matrixTaskRepository.save(matrixTask);
        if (user.getMatrixTasks() == null) {
            user.setMatrixTasks(new java.util.ArrayList<>());
        }
        user.getMatrixTasks().add(matrixTask);
        userRepository.save(user);
        return matrixTask;
    }

    public List<MatrixTask> getMatrixTasksByUserId(String userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (!userOpt.isPresent()) {
            throw new RuntimeException("User not found");
        }
        return userOpt.get().getMatrixTasks();
    }

    public MatrixTask getMatrixTaskById(String userId, String taskId) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (!userOpt.isPresent()) {
            throw new RuntimeException("User not found");
        }
        User user = userOpt.get();
        return user.getMatrixTasks().stream()
                .filter(task -> task.getId().equals(taskId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("MatrixTask not found"));
    }

    public MatrixTask updateMatrixTask(String userId, String taskId, MatrixTask updatedTask) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (!userOpt.isPresent()) {
            throw new RuntimeException("User not found");
        }
        User user = userOpt.get();

        MatrixTask taskToUpdate = user.getMatrixTasks().stream()
                .filter(task -> task.getId().equals(taskId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("MatrixTask not found"));

        taskToUpdate.setDescription(updatedTask.getDescription());
        taskToUpdate.setQuadrant(updatedTask.getQuadrant());
        taskToUpdate.setCompleted(updatedTask.isCompleted());

        matrixTaskRepository.save(taskToUpdate);
        return taskToUpdate;
    }

    public void deleteMatrixTask(String userId, String taskId) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (!userOpt.isPresent()) {
            throw new RuntimeException("User not found");
        }
        User user = userOpt.get();

        MatrixTask taskToDelete = user.getMatrixTasks().stream()
                .filter(task -> task.getId().equals(taskId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("MatrixTask not found"));

        user.getMatrixTasks().remove(taskToDelete);
        matrixTaskRepository.delete(taskToDelete);
        userRepository.save(user);
    }
}
