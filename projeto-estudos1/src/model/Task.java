package model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Task {
    private String titulo;
    private String descricao;
    private String categoria;
    private LocalDate dataVencimento;
    private boolean concluida;

    public Task(String titulo, String descricao, String categoria, LocalDate dataVencimento) {
        this(titulo, descricao, categoria, dataVencimento, false);
    }

    public Task(String titulo, String descricao, String categoria, LocalDate dataVencimento, boolean concluida) {
        this.titulo = titulo;
        this.descricao = descricao;
        this.categoria = categoria;
        this.dataVencimento = dataVencimento;
        this.concluida = concluida;
    }

    public String getTituloRaw() { return titulo; }
    public String getDescricao() { return descricao; }
    public String getCategoria() { return categoria; }
    public LocalDate getDataVencimento() { return dataVencimento; }

    public void setTitulo(String titulo) { this.titulo = titulo; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public void setCategoria(String categoria) { this.categoria = categoria; }
    public void setDataVencimento(LocalDate dataVencimento) { this.dataVencimento = dataVencimento; }

    public void alternarConclusao() { this.concluida = !this.concluida; }
    public boolean isConcluida() { return concluida; }

    public String getTitulo() {
        String status = concluida ? "[CONCLUIDA]" : "[PENDENTE] ";
        return String.format("%-20s | %-20s | %-12s | %-10s | %s",
            limitar(titulo, 20), limitar(descricao, 20), limitar(categoria, 12),
            dataVencimento.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
            status);
    }

    private String limitar(String texto, int tamanho) {
        if (texto.length() <= tamanho) return texto;
        return texto.substring(0, Math.max(0, tamanho - 3)) + "...";
    }
}
