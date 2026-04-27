package com.tarefas.api.controller;

import com.tarefas.api.model.Tarefa;
import com.tarefas.api.service.TarefaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/tarefas")
public class TarefaController {

    private final TarefaService tarefaService;

    public TarefaController(TarefaService tarefaService) {
        this.tarefaService = tarefaService;
    }

    @GetMapping
    public List<Tarefa> listar() {
        return tarefaService.listar();
    }

    @PostMapping
    public ResponseEntity<Tarefa> adicionar(@RequestBody Map<String, String> body) {
        String descricao = body.get("descricao");
        if (descricao == null || descricao.trim().isEmpty())
            return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(tarefaService.adicionar(descricao));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> remover(@PathVariable Long id) {
        try {
            tarefaService.remover(id);
            return ResponseEntity.ok("Tarefa removida com sucesso");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
