package com.example.backend.services;

import com.example.backend.entities.Todo;
import com.example.backend.entities.User;
import com.example.backend.repositories.TodoRepository;
import com.example.backend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TodoService {

    @Autowired
    private TodoRepository todoRepository;

    @Autowired
    private UserRepository userRepository;

    public Todo createTodoForUser(String userId, Todo todo) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (!userOpt.isPresent()) {
            throw new RuntimeException("User not found");
        }
        User user = userOpt.get();
        todoRepository.save(todo);
        user.getTodos().add(todo);
        userRepository.save(user);
        return todo;
    }

    public List<Todo> getTodosByUserId(String userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (!userOpt.isPresent()) {
            throw new RuntimeException("User not found");
        }
        return userOpt.get().getTodos();
    }

    public Todo getTodoById(String userId, String todoId) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (!userOpt.isPresent()) {
            throw new RuntimeException("User not found");
        }
        User user = userOpt.get();
        return user.getTodos().stream()
                .filter(todo -> todo.getId().equals(todoId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Todo not found"));
    }

    public Todo updateTodo(String userId, String todoId, Todo updatedTodo) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (!userOpt.isPresent()) {
            throw new RuntimeException("User not found");
        }
        User user = userOpt.get();

        Todo todoToUpdate = user.getTodos().stream()
                .filter(todo -> todo.getId().equals(todoId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Todo not found"));

        todoToUpdate.setDescription(updatedTodo.getDescription());
        todoToUpdate.setCompleted(updatedTodo.isCompleted());
        todoRepository.save(todoToUpdate);
        return todoToUpdate;
    }

    public void deleteTodo(String userId, String todoId) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (!userOpt.isPresent()) {
            throw new RuntimeException("User not found");
        }
        User user = userOpt.get();

        Todo todoToDelete = user.getTodos().stream()
                .filter(todo -> todo.getId().equals(todoId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Todo not found"));

        user.getTodos().remove(todoToDelete);
        todoRepository.delete(todoToDelete);
        userRepository.save(user);
    }
}

