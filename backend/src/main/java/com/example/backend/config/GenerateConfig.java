package com.example.backend.config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class GenerateConfig {
    @Value("${gemini.api.key}")
    private String apiKey;
    @Bean
    public WebClient geminiWebClient() {
        return WebClient.builder()
                .baseUrl("https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key="+apiKey)
                .defaultHeader("Content-Type", "application/json")
                .build();
    }
}
