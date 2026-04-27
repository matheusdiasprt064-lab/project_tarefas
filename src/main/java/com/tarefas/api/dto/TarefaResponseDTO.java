package com.tarefas.api.dto;

import com.tarefas.api.model.Tarefa;

public class TarefaResponseDTO {
    private Long id;
    private String descricao;
    private String usuario;

    public TarefaResponseDTO(Tarefa tarefa) {
        this.id = tarefa.getId();
        this.descricao = tarefa.getDescricao();
        this.usuario = tarefa.getUsuario().getUsername();
    }

    public Long getId() { return id; }
    public String getDescricao() { return descricao; }
    public String getUsuario() { return usuario; }
}
