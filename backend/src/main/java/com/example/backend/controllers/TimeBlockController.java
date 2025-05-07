package com.example.backend.controllers;

import com.example.backend.entities.TimeBlock;
import com.example.backend.services.TimeBlockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users/{userId}/timeblocks")
public class TimeBlockController {

    @Autowired
    private TimeBlockService timeBlockService;

    @PostMapping
    public ResponseEntity<TimeBlock> createTimeBlock(
            @PathVariable String userId,
            @RequestBody TimeBlock timeBlock) {
        TimeBlock createdBlock = timeBlockService.createTimeBlockForUser(userId, timeBlock);
        return ResponseEntity.ok(createdBlock);
    }

    @GetMapping("/{blockId}")
    public ResponseEntity<TimeBlock> getTimeBlockById(
            @PathVariable String userId,
            @PathVariable String blockId) {
        TimeBlock timeBlock = timeBlockService.getTimeBlockById(userId, blockId);
        return ResponseEntity.ok(timeBlock);
    }

    @GetMapping
    public ResponseEntity<List<TimeBlock>> getAllTimeBlocks(
            @PathVariable String userId) {
        List<TimeBlock> timeBlocks = timeBlockService.getTimeBlocksByUserId(userId);
        return ResponseEntity.ok(timeBlocks);
    }

    @PutMapping("/{blockId}")
    public ResponseEntity<TimeBlock> updateTimeBlock(
            @PathVariable String userId,
            @PathVariable String blockId,
            @RequestBody TimeBlock updatedBlock) {
        TimeBlock timeBlock = timeBlockService.updateTimeBlock(userId, blockId, updatedBlock);
        return ResponseEntity.ok(timeBlock);
    }

    @DeleteMapping("/{blockId}")
    public ResponseEntity<Void> deleteTimeBlock(
            @PathVariable String userId,
            @PathVariable String blockId) {
        timeBlockService.deleteTimeBlock(userId, blockId);
        return ResponseEntity.noContent().build();
    }
}
