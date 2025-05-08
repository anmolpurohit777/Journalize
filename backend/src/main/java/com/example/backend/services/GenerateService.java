//package com.example.backend.services;
//
//import com.fasterxml.jackson.databind.JsonNode;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Service;
//import org.springframework.web.reactive.function.client.WebClient;
//import reactor.core.publisher.Mono;
//
//
//import java.util.Map;
//
//@Service
//public class GenerateService {
//
//    private final WebClient webClient;
//    ObjectMapper objectMapper = new ObjectMapper();
//
//
//    @Value("${gemini.api.key}")
//    private String apiKey;
//
//    public GenerateService(WebClient geminiWebClient) {
//        this.webClient = geminiWebClient;
//    }
//
//    public Mono<String> generateContent(String userPrompt) {
//        Map<String, Object> requestBody = Map.of(
//                "contents", new Object[] {
//                        Map.of("parts", new Object[] {
//                                Map.of("text", userPrompt)
//                        })
//                }
//        );
//
//        return webClient.post()
//                .uri(uriBuilder -> uriBuilder.queryParam("key", apiKey).build())
//                .bodyValue(requestBody)
//                .retrieve()
//                .bodyToMono(Map.class)
//                .map(response -> {
//                    var candidates = (java.util.List<Map<String, Object>>) response.get("candidates");
//                    if (candidates != null && !candidates.isEmpty()) {
//                        var content = (Map<String, Object>) candidates.get(0).get("content");
//                        var parts = (java.util.List<Map<String, Object>>) content.get("parts");
//                        return (String) parts.get(0).get("text");
//                    }
//                    return "No response.";
//                });
//    }
//
//    @Scheduled(fixedRate = 60000)
//    public void callGeminiApi() {
//        String inputString = """
//            {
//              "contents": [
//                {
//                  "parts": [
//                    { "text": "Give 5 fruits name" }
//                  ]
//                }
//              ]
//            }
//        """;
//        callGeminiApi(inputString);
//    }
//
//    public void callGeminiApi(String inputString) {
//        webClient.post()
//                .bodyValue(inputString)
//                .retrieve()
//                .bodyToMono(String.class)
//                .doOnTerminate(() -> System.out.println("Gemini API call completed"))
//                .subscribe(response -> {
//                    try {
//                        JsonNode root = objectMapper.readTree(response);
//                        String result = root
//                                .path("candidates").get(0)
//                                .path("content")
//                                .path("parts").get(0)
//                                .path("text")
//                                .asText();
//
//                        System.out.println("Gemini Result: " + result);
//                    } catch (Exception e) {
//                        System.err.println("Failed to parse response: " + e.getMessage());
//                    }
//                }, error -> {
//                    System.err.println("API Error: " + error.getMessage());
//                });
//    }
//}
//
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
    public void callGeminiApi() {
        String userId = "681ac3e662b36c4daf5ed916";
        String todayDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        DailyLog dailyLog = findDailyLogForUser(userId, todayDate);

        if (dailyLog != null) {
            String journalPrompt = generateJournalPrompt(dailyLog);
            generateContent(journalPrompt).subscribe(journalEntry -> {
                dailyLog.setJournalEntry(journalEntry);
                dailyLogRepository.save(dailyLog); // Save updated DailyLog back to DB
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
        prompt.append("Using the following user productivity data, generate a daily journal entry in pure Markdown format. The journal should summarize the day clearly and concisely. Use headings, bullet points, and emphasis where appropriate. Do not include any explanations or text outside the Markdown format.\n\n");

        prompt.append("### Data:\n");

        prompt.append("- Completed Todos:\n");
        dailyLog.getCompletedTodos().forEach(todo -> prompt.append("  - ").append(todo).append("\n"));

        prompt.append("- Kanban Card Moves:\n");
        dailyLog.getKanbanActivity().forEach(kanban -> prompt.append("  - ").append(kanban).append("\n"));

        prompt.append("- Urgent and Important tasks Completed:\n");
        dailyLog.getCompletedUrgentTasks().forEach(kanban -> prompt.append("  - ").append(kanban).append("\n"));

        prompt.append("\nMake sure the output is **valid Markdown only**.\n");

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

