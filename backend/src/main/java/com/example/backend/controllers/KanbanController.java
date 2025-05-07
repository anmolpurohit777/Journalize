package com.example.backend.controllers;

import com.example.backend.entities.Kanban;
import com.example.backend.services.KanbanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users/{userId}/kanbans")
public class KanbanController {

    @Autowired
    private KanbanService kanbanService;

    @PostMapping
    public ResponseEntity<Kanban> createKanban(
            @PathVariable String userId,
            @RequestBody Kanban kanban) {
        Kanban createdKanban = kanbanService.createKanbanForUser(userId, kanban);
        return ResponseEntity.ok(createdKanban);
    }

    @GetMapping("/{kanbanId}")
    public ResponseEntity<Kanban> getKanbanById(
            @PathVariable String userId,
            @PathVariable String kanbanId) {
        Kanban kanban = kanbanService.getKanbanById(userId, kanbanId);
        return ResponseEntity.ok(kanban);
    }

    @GetMapping
    public ResponseEntity<List<Kanban>> getAllKanbans(
            @PathVariable String userId) {
        List<Kanban> kanbans = kanbanService.getKanbansByUserId(userId);
        return ResponseEntity.ok(kanbans);
    }

    @PutMapping("/{kanbanId}")
    public ResponseEntity<Kanban> updateKanban(
            @PathVariable String userId,
            @PathVariable String kanbanId,
            @RequestBody Kanban updatedKanban) {
        Kanban kanban = kanbanService.updateKanban(userId, kanbanId, updatedKanban);
        return ResponseEntity.ok(kanban);
    }

    @DeleteMapping("/{kanbanId}")
    public ResponseEntity<Void> deleteKanban(
            @PathVariable String userId,
            @PathVariable String kanbanId) {
        kanbanService.deleteKanban(userId, kanbanId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<Kanban>> getKanbansByStatus(
            @PathVariable String userId,
            @PathVariable String status) {
        List<Kanban> kanbans = kanbanService.getKanbansByStatus(status);
        return ResponseEntity.ok(kanbans);
    }
}
