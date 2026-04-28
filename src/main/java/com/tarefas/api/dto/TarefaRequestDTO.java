package com.tarefas.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

public class TarefaRequestDTO {
    @Size(max = 100, message = "Titulo deve ter no maximo 100 caracteres")
    private String titulo;

    @NotBlank(message = "Descricao e obrigatoria")
    @Size(max = 500, message = "Descricao deve ter no maximo 500 caracteres")
    private String descricao;

    private Boolean concluida;
    private LocalDate dataVencimento;

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public Boolean getConcluida() { return concluida; }
    public void setConcluida(Boolean concluida) { this.concluida = concluida; }
    public LocalDate getDataVencimento() { return dataVencimento; }
    public void setDataVencimento(LocalDate dataVencimento) { this.dataVencimento = dataVencimento; }
}
