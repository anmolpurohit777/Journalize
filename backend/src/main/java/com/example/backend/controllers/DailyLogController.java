package com.example.backend.controllers;

import com.example.backend.entities.DailyLog;
import com.example.backend.services.DailyLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/users/{userId}/daily-logs")
public class DailyLogController {

    @Autowired
    private DailyLogService dailyLogService;

    @PostMapping
    public ResponseEntity<DailyLog> logUserActivity(
            @PathVariable String userId,
            @RequestBody DailyLog dailyLogInput) {
        try {
            DailyLog result = dailyLogService.logUserActivity(userId, dailyLogInput);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/{date}")
    public ResponseEntity<DailyLog> getDailyLog(
            @PathVariable String userId,
            @PathVariable String date) {
        Optional<DailyLog> dailyLog = dailyLogService.getDailyLogForUserAndDate(userId, date);
        return dailyLog.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
