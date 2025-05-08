package com.example.backend.entities;

import org.springframework.data.annotation.Id;

import java.util.ArrayList;
import java.util.List;

public class DailyLog {

    @Id
    private String id;
    private String date;
    private List<String> completedTodos=new ArrayList<>();
    private List<String> kanbanActivity=new ArrayList<>();
    private List<String> completedUrgentTasks=new ArrayList<>();
    private String journalEntry;

    public DailyLog(String id, String date, List<String> completedTodos, List<String> kanbanActivity, List<String> timeBlocksUsed, List<String> completedUrgentTasks, String journalEntry) {
        this.id = id;
        this.date = date;
        this.completedTodos = completedTodos;
        this.kanbanActivity = kanbanActivity;
        this.completedUrgentTasks = completedUrgentTasks;
        this.journalEntry = journalEntry;
    }

    public DailyLog() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<String> getCompletedTodos() {
        return completedTodos;
    }

    public void setCompletedTodos(List<String> completedTodos) {
        this.completedTodos = completedTodos;
    }

    public List<String> getKanbanActivity() {
        return kanbanActivity;
    }

    public void setKanbanActivity(List<String> kanbanActivity) {
        this.kanbanActivity = kanbanActivity;
    }

    public List<String> getCompletedUrgentTasks() {
        return completedUrgentTasks;
    }

    public void setCompletedUrgentTasks(List<String> completedUrgentTasks) {
        this.completedUrgentTasks = completedUrgentTasks;
    }

    public String getJournalEntry() {
        return journalEntry;
    }

    public void setJournalEntry(String journalEntry) {
        this.journalEntry = journalEntry;
    }
}