package com.example.backend.services;

import com.example.backend.entities.DailyLog;
import com.example.backend.entities.User;
import com.example.backend.repositories.DailyLogRepository;
import com.example.backend.repositories.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Service
public class GenerateService {

    private final WebClient webClient;
    private final UserRepository userRepository; // Assuming you have UserRepository to fetch users.
    private final DailyLogRepository dailyLogRepository; // Assuming DailyLogRepository exists for DB operations.

    ObjectMapper objectMapper = new ObjectMapper();

    @Value("${gemini.api.key}")
    private String apiKey;

    public GenerateService(WebClient webClient, UserRepository userRepository, DailyLogRepository dailyLogRepository) {
        this.webClient = webClient;
        this.userRepository = userRepository;
        this.dailyLogRepository = dailyLogRepository;
    }

    @Scheduled(cron = "0 0 22 * * ?")
//    @Scheduled(cron = "0 */5 * * * ?")
//    @Scheduled(cron = "0 * * * * ?")
    public void callGeminiApi() {
        String userId = "681ac3e662b36c4daf5ed916";     // Currently Generating Journal for only 1 user, using for-loop on User's List we can generate for all
        String todayDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        DailyLog dailyLog = findDailyLogForUser(userId, todayDate);

        if (dailyLog != null) {
            String journalPrompt = generateJournalPrompt(dailyLog);
            generateContent(journalPrompt).subscribe(journalEntry -> {
                dailyLog.setJournalEntry(journalEntry);
                dailyLogRepository.save(dailyLog);
                System.out.println(journalEntry);
            });
        } else {
            System.out.println("No DailyLog found for the user on " + todayDate);
        }
    }

    private DailyLog findDailyLogForUser(String userId, String date) {
        User user = userRepository.findById(userId).orElse(null);

        if (user != null) {
            return user.getDailyLogs().stream()
                    .filter(log -> log.getDate().equals(date))
                    .findFirst()
                    .orElse(null);
        }
        return null;
    }

private String generateJournalPrompt(DailyLog dailyLog) {
    StringBuilder prompt = new StringBuilder();
    prompt.append("Using the following user productivity data, create a detailed, creative, and humanized daily journal entry in pure Markdown format. The journal should feel like a reflective and insightful report of the day, not just a dry summary. Use headings, bullet points, subheadings, quotes, emphasis, and other Markdown features to enhance readability and appeal. The tone should be warm, reflective, and personal.\n\n");
    prompt.append("Maintain Proper formatting throughout the response\n\n");
    prompt.append(dailyLog.getDate()).append("\n\n");
    prompt.append("First write a daily quote\n\n");

    prompt.append("Overview\n");
    prompt.append("Write Overview of the Day using given data.\n\n");

    if (!dailyLog.getCompletedTodos().isEmpty()) {
        prompt.append("Completed Todos\n");
        prompt.append("The following todos were successfully completed today:\n");
        dailyLog.getCompletedTodos().forEach(todo -> prompt.append("-").append(todo).append("\n"));
        prompt.append("\n");
    }

    if (!dailyLog.getKanbanActivity().isEmpty()) {
        prompt.append("Kanban Board Updates\n");
        prompt.append("Here’s how my Kanban board looked after today’s work:\n");
        dailyLog.getKanbanActivity().forEach(kanban -> prompt.append("-").append(kanban).append("\n"));
        prompt.append("\n");
    }

    if (!dailyLog.getCompletedUrgentTasks().isEmpty()) {
        prompt.append("Urgent & Important Tasks\n");
        prompt.append("I completed the following critical tasks that were urgent and important:\n");
        dailyLog.getCompletedUrgentTasks().forEach(task -> prompt.append("-Completed: ").append(task).append("\n"));
        prompt.append("\n");
    }

    prompt.append("Detailed Work Breakdown\n");
    prompt.append("Give detailed work breakdown\n\n");

    prompt.append("End of the Day Reflection\n");

    prompt.append("Give result in only markdown format, dont add anything else\n");


    return prompt.toString();
}


    public Mono<String> generateContent(String userPrompt) {
        Map<String, Object> requestBody = Map.of(
                "contents", new Object[]{
                        Map.of("parts", new Object[]{
                                Map.of("text", userPrompt)
                        })
                }
        );

        return webClient.post()
                .uri(uriBuilder -> uriBuilder.queryParam("key", apiKey).build())
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(Map.class)
                .map(response -> {
                    var candidates = (java.util.List<Map<String, Object>>) response.get("candidates");
                    if (candidates != null && !candidates.isEmpty()) {
                        var content = (Map<String, Object>) candidates.get(0).get("content");
                        var parts = (java.util.List<Map<String, Object>>) content.get("parts");
                        return (String) parts.get(0).get("text");
                    }
                    return "No response.";
                });
    }
}

