package com.tarefas.api.dto;

import com.tarefas.api.model.Tarefa;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class TarefaResponseDTO {
    private Long id;
    private String titulo;
    private String descricao;
    private boolean concluida;
    private LocalDate dataVencimento;
    private LocalDateTime dataCriacao;
    private String usuario;

    public TarefaResponseDTO(Tarefa tarefa) {
        this.id = tarefa.getId();
        this.titulo = tarefa.getTitulo();
        this.descricao = tarefa.getDescricao();
        this.concluida = tarefa.isConcluida();
        this.dataVencimento = tarefa.getDataVencimento();
        this.dataCriacao = tarefa.getDataCriacao();
        this.usuario = tarefa.getUsuario().getUsername();
    }

    public Long getId() { return id; }
    public String getTitulo() { return titulo; }
    public String getDescricao() { return descricao; }
    public boolean isConcluida() { return concluida; }
    public LocalDate getDataVencimento() { return dataVencimento; }
    public LocalDateTime getDataCriacao() { return dataCriacao; }
    public String getUsuario() { return usuario; }
}
