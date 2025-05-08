package com.example.backend.services;

import com.example.backend.entities.DailyLog;
import com.example.backend.entities.User;
import com.example.backend.repositories.DailyLogRepository;
import com.example.backend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class DailyLogService {

    @Autowired
    private DailyLogRepository dailyLogRepository;

    @Autowired
    private UserRepository userRepository;

    public DailyLog logUserActivity(String userId, DailyLog dailyLogInput) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        String todayDate = LocalDate.now().toString();

        Optional<DailyLog> existingLogOpt = this.findByUserIdAndDate(userId, todayDate);

        DailyLog log;
        if (existingLogOpt.isPresent()) {
            log = existingLogOpt.get();

            log.getCompletedTodos().addAll(dailyLogInput.getCompletedTodos());
            log.getKanbanActivity().addAll(dailyLogInput.getKanbanActivity());
            log.getCompletedUrgentTasks().addAll(dailyLogInput.getCompletedUrgentTasks());

            log.setJournalEntry(existingLogOpt.get().getJournalEntry());
        } else {
            log = new DailyLog();
            log.setDate(todayDate);

            log.getCompletedTodos().addAll(dailyLogInput.getCompletedTodos());
            log.getKanbanActivity().addAll(dailyLogInput.getKanbanActivity());
            log.getCompletedUrgentTasks().addAll(dailyLogInput.getCompletedUrgentTasks());

            log.setJournalEntry(dailyLogInput.getJournalEntry());
        }


        DailyLog savedLog = dailyLogRepository.save(log);

        if (user.getDailyLogs().stream().noneMatch(dl -> dl.getId().equals(savedLog.getId()))) {
            user.getDailyLogs().add(savedLog);
            userRepository.save(user);
        }

        return savedLog;
    }

    public Optional<DailyLog> getDailyLogForUserAndDate(String userId, String date) {
        return this.findByUserIdAndDate(userId, date);
    }

    public Optional<DailyLog> findByUserIdAndDate(String userId, String date) {
        User user = userRepository.findById(userId).orElse(null);

        if (user != null) {
            return user.getDailyLogs().stream()
                    .filter(log -> log.getDate().equals(date))
                    .findFirst();  // Returns Optional<DailyLog>
        }

        return Optional.empty();
    }

}

