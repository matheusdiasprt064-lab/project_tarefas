package com.tarefas.api.controller;

import com.tarefas.api.dto.TarefaRequestDTO;
import com.tarefas.api.dto.TarefaResponseDTO;
import com.tarefas.api.service.TarefaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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
    @Operation(summary = "Listar tarefas do usuario logado")
    public List<TarefaResponseDTO> listar() {
        return tarefaService.listar();
    }

    @PostMapping
    @Operation(summary = "Adicionar nova tarefa")
    public ResponseEntity<TarefaResponseDTO> adicionar(@Valid @RequestBody TarefaRequestDTO dto) {
        return ResponseEntity.ok(tarefaService.adicionar(dto));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar tarefa")
    public ResponseEntity<TarefaResponseDTO> atualizar(@PathVariable Long id, @Valid @RequestBody TarefaRequestDTO dto) {
        return ResponseEntity.ok(tarefaService.atualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Remover tarefa")
    public ResponseEntity<String> remover(@PathVariable Long id) {
        tarefaService.remover(id);
        return ResponseEntity.ok("Tarefa removida com sucesso");
    }
}
