package com.tarefas.api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class HomeController {

    @GetMapping("/")
    public Map<String, String> home() {
        return Map.of(
            "api", "Task Manager API",
            "docs", "https://project-tarefas.onrender.com/swagger-ui/index.html",
            "status", "https://project-tarefas.onrender.com/status"
        );
    }

    @GetMapping("/status")
    public Map<String, String> status() {
        return Map.of(
            "status", "online",
            "version", "1.0.0"
        );
    }
}
