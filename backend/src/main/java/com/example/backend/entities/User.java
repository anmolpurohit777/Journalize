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

    @DBRef
    private List<TimeBlock> timeBlocks=new ArrayList<>();

    @DBRef
    private List<Kanban> kanbans=new ArrayList<>();

    public User(List<TimeBlock> timeBlocks, String id, String username, String email, String password, List<Kanban> kanbans, List<MatrixTask> matrixTasks, List<Todo> todos) {
        this.timeBlocks = timeBlocks;
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.kanbans = kanbans;
        this.matrixTasks = matrixTasks;
        this.todos = todos;
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

    public List<TimeBlock> getTimeBlocks() {
        return timeBlocks;
    }

    public void setTimeBlocks(List<TimeBlock> timeBlocks) {
        this.timeBlocks = timeBlocks;
    }

    public List<Kanban> getKanbans() {
        return kanbans;
    }

    public void setKanbans(List<Kanban> kanbans) {
        this.kanbans = kanbans;
    }
}
