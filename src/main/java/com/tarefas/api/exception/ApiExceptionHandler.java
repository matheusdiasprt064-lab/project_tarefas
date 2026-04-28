package com.tarefas.api.exception;

import com.tarefas.api.dto.ErroResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErroResponseDTO> handleValidation(MethodArgumentNotValidException e) {
        Map<String, String> campos = new LinkedHashMap<>();
        e.getBindingResult().getFieldErrors()
            .forEach(erro -> campos.put(erro.getField(), erro.getDefaultMessage()));
        return erro(HttpStatus.BAD_REQUEST, "Dados invalidos", "Revise os campos enviados.", campos);
    }

    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public ResponseEntity<ErroResponseDTO> handleNotFound(RecursoNaoEncontradoException e) {
        return erro(HttpStatus.NOT_FOUND, "Recurso nao encontrado", e.getMessage());
    }

    @ExceptionHandler(AcessoNegadoException.class)
    public ResponseEntity<ErroResponseDTO> handleForbidden(AcessoNegadoException e) {
        return erro(HttpStatus.FORBIDDEN, "Acesso negado", e.getMessage());
    }

    @ExceptionHandler(ConflitoException.class)
    public ResponseEntity<ErroResponseDTO> handleConflict(ConflitoException e) {
        return erro(HttpStatus.CONFLICT, "Conflito", e.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErroResponseDTO> handleBadRequest(IllegalArgumentException e) {
        return erro(HttpStatus.BAD_REQUEST, "Requisicao invalida", e.getMessage());
    }

    private ResponseEntity<ErroResponseDTO> erro(HttpStatus status, String erro, String mensagem) {
        return erro(status, erro, mensagem, null);
    }

    private ResponseEntity<ErroResponseDTO> erro(HttpStatus status, String erro, String mensagem, Map<String, String> campos) {
        return ResponseEntity.status(status).body(new ErroResponseDTO(status.value(), erro, mensagem, campos));
    }
}
