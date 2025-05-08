package com.example.backend.controllers;

import com.example.backend.services.GenerateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/generate")
public class GenerateController {

    @Autowired
    private GenerateService geminiService;

    @PostMapping
    public Mono<String> askGemini(@RequestBody String prompt) {
        return geminiService.generateContent(prompt);
    }
}

