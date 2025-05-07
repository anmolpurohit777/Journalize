package com.example.backend.controllers;

import com.example.backend.entities.MatrixTask;
import com.example.backend.services.MatrixTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users/{userId}/matrix")
public class MatrixTaskController {

    @Autowired
    private MatrixTaskService matrixTaskService;

    @PostMapping
    public ResponseEntity<MatrixTask> createMatrixTask(
            @PathVariable String userId,
            @RequestBody MatrixTask matrixTask) {
        MatrixTask createdTask = matrixTaskService.createMatrixTaskForUser(userId, matrixTask);
        return ResponseEntity.ok(createdTask);
    }

    @GetMapping("/{taskId}")
    public ResponseEntity<MatrixTask> getMatrixTaskById(
            @PathVariable String userId,
            @PathVariable String taskId) {
        MatrixTask matrixTask = matrixTaskService.getMatrixTaskById(userId, taskId);
        return ResponseEntity.ok(matrixTask);
    }

    @GetMapping
    public ResponseEntity<List<MatrixTask>> getAllMatrixTasks(
            @PathVariable String userId) {
        List<MatrixTask> matrixTasks = matrixTaskService.getMatrixTasksByUserId(userId);
        return ResponseEntity.ok(matrixTasks);
    }

    @PutMapping("/{taskId}")
    public ResponseEntity<MatrixTask> updateMatrixTask(
            @PathVariable String userId,
            @PathVariable String taskId,
            @RequestBody MatrixTask updatedTask) {
        MatrixTask matrixTask = matrixTaskService.updateMatrixTask(userId, taskId, updatedTask);
        return ResponseEntity.ok(matrixTask);
    }

    @DeleteMapping("/{taskId}")
    public ResponseEntity<Void> deleteMatrixTask(
            @PathVariable String userId,
            @PathVariable String taskId) {
        matrixTaskService.deleteMatrixTask(userId, taskId);
        return ResponseEntity.noContent().build();
    }
}
