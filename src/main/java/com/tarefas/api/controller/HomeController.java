package com.tarefas.api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "Task Manager API 🚀 - Documentação: /swagger-ui/index.html";
    }
}
