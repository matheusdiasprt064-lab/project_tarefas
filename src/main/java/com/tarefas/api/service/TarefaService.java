package com.tarefas.api.service;

import com.tarefas.api.dto.TarefaRequestDTO;
import com.tarefas.api.dto.TarefaResponseDTO;
import com.tarefas.api.exception.AcessoNegadoException;
import com.tarefas.api.exception.RecursoNaoEncontradoException;
import com.tarefas.api.model.Tarefa;
import com.tarefas.api.model.Usuario;
import com.tarefas.api.repository.TarefaRepository;
import com.tarefas.api.repository.UsuarioRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TarefaService {

    private final TarefaRepository tarefaRepository;
    private final UsuarioRepository usuarioRepository;

    public TarefaService(TarefaRepository tarefaRepository, UsuarioRepository usuarioRepository) {
        this.tarefaRepository = tarefaRepository;
        this.usuarioRepository = usuarioRepository;
    }

    private Usuario getUsuarioLogado() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuario nao encontrado"));
    }

    public List<TarefaResponseDTO> listar() {
        return tarefaRepository.findByUsuario(getUsuarioLogado())
                .stream().map(TarefaResponseDTO::new).toList();
    }

    public TarefaResponseDTO adicionar(TarefaRequestDTO dto) {
        Tarefa tarefa = new Tarefa();
        aplicarDados(tarefa, dto);
        tarefa.setUsuario(getUsuarioLogado());
        return new TarefaResponseDTO(tarefaRepository.save(tarefa));
    }

    public TarefaResponseDTO atualizar(Long id, TarefaRequestDTO dto) {
        Tarefa tarefa = buscarTarefaDoUsuario(id);
        aplicarDados(tarefa, dto);
        return new TarefaResponseDTO(tarefaRepository.save(tarefa));
    }

    public void remover(Long id) {
        Tarefa tarefa = buscarTarefaDoUsuario(id);
        tarefaRepository.delete(tarefa);
    }

    private Tarefa buscarTarefaDoUsuario(Long id) {
        Tarefa tarefa = tarefaRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Tarefa nao encontrada"));
        if (!tarefa.getUsuario().getUsername().equals(getUsuarioLogado().getUsername())) {
            throw new AcessoNegadoException("Sem permissao para acessar esta tarefa");
        }
        return tarefa;
    }

    private void aplicarDados(Tarefa tarefa, TarefaRequestDTO dto) {
        tarefa.setTitulo(limpar(dto.getTitulo()));
        tarefa.setDescricao(dto.getDescricao().trim());
        tarefa.setDataVencimento(dto.getDataVencimento());
        if (dto.getConcluida() != null) {
            tarefa.setConcluida(dto.getConcluida());
        }
    }

    private String limpar(String valor) {
        if (valor == null || valor.trim().isEmpty()) return null;
        return valor.trim();
    }
}
