package com.tarefas.api.controller;

import com.tarefas.api.dto.TarefaRequestDTO;
import com.tarefas.api.dto.TarefaResponseDTO;
import com.tarefas.api.service.TarefaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tarefas")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Tarefas")
public class TarefaController {

    private final TarefaService tarefaService;

    public TarefaController(TarefaService tarefaService) {
        this.tarefaService = tarefaService;
    }

    @GetMapping
    @Operation(summary = "Listar tarefas do usuário logado")
    public List<TarefaResponseDTO> listar() {
        return tarefaService.listar();
    }

    @PostMapping
    @Operation(summary = "Adicionar nova tarefa")
    public ResponseEntity<TarefaResponseDTO> adicionar(@RequestBody TarefaRequestDTO dto) {
        if (dto.getDescricao() == null || dto.getDescricao().trim().isEmpty())
            return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(tarefaService.adicionar(dto.getDescricao()));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar tarefa")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @RequestBody TarefaRequestDTO dto) {
        try {
            return ResponseEntity.ok(tarefaService.atualizar(id, dto.getDescricao()));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Remover tarefa")
    public ResponseEntity<?> remover(@PathVariable Long id) {
        try {
            tarefaService.remover(id);
            return ResponseEntity.ok("Tarefa removida com sucesso");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
