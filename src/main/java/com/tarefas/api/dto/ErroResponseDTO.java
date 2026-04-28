package com.tarefas.api.dto;

import java.time.LocalDateTime;
import java.util.Map;

public class ErroResponseDTO {
    private final LocalDateTime timestamp = LocalDateTime.now();
    private final int status;
    private final String erro;
    private final String mensagem;
    private final Map<String, String> campos;

    public ErroResponseDTO(int status, String erro, String mensagem) {
        this(status, erro, mensagem, null);
    }

    public ErroResponseDTO(int status, String erro, String mensagem, Map<String, String> campos) {
        this.status = status;
        this.erro = erro;
        this.mensagem = mensagem;
        this.campos = campos;
    }

    public LocalDateTime getTimestamp() { return timestamp; }
    public int getStatus() { return status; }
    public String getErro() { return erro; }
    public String getMensagem() { return mensagem; }
    public Map<String, String> getCampos() { return campos; }
}
