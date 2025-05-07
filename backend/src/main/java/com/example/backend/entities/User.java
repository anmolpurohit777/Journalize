package com.example.backend.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "users")
public class User {

    @Id
    private String id;
    private String username;
    private String email;
    private String password;

    @DBRef
    private List<Todo> todos=new ArrayList<>();

    @DBRef
    private List<MatrixTask> matrixTasks=new ArrayList<>();

    public User(String id, String email, String username, String password, List<Todo> todos, List<MatrixTask> matrixTasks) {
        this.id = id;
        this.email = email;
        this.username = username;
        this.password = password;
        this.todos = todos;
        this.matrixTasks = matrixTasks;
    }

    public User() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Todo> getTodos() {
        return todos;
    }

    public void setTodos(List<Todo> todos) {
        this.todos = todos;
    }

    public List<MatrixTask> getMatrixTasks() {
        return matrixTasks;
    }

    public void setMatrixTasks(List<MatrixTask> matrixTasks) {
        this.matrixTasks = matrixTasks;
    }
}
