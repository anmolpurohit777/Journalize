package com.example.backend.controllers;

import com.example.backend.entities.Todo;
import com.example.backend.services.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users/{userId}/todos")
public class TodoController {

    @Autowired
    private TodoService todoService;

    @PostMapping
    public ResponseEntity<Todo> createTodo(
            @PathVariable String userId,
            @RequestBody Todo todo) {
        Todo createdTodo = todoService.createTodoForUser(userId, todo);
        return ResponseEntity.ok(createdTodo);
    }

    @GetMapping("/{todoId}")
    public ResponseEntity<Todo> getTodoById(
            @PathVariable String userId,
            @PathVariable String todoId) {
        Todo todo = todoService.getTodoById(userId, todoId);
        return ResponseEntity.ok(todo);
    }

    @GetMapping
    public ResponseEntity<List<Todo>> getAllTodos(
            @PathVariable String userId) {
        List<Todo> todos = todoService.getTodosByUserId(userId);
        return ResponseEntity.ok(todos);
    }

    @PutMapping("/{todoId}")
    public ResponseEntity<Todo> updateTodo(
            @PathVariable String userId,
            @PathVariable String todoId,
            @RequestBody Todo updatedTodo) {
        Todo todo = todoService.updateTodo(userId, todoId, updatedTodo);
        return ResponseEntity.ok(todo);
    }

    @DeleteMapping("/{todoId}")
    public ResponseEntity<Void> deleteTodo(
            @PathVariable String userId,
            @PathVariable String todoId) {
        todoService.deleteTodo(userId, todoId);
        return ResponseEntity.noContent().build();
    }
}