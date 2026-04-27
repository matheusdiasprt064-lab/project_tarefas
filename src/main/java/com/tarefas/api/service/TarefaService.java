package com.tarefas.api.service;

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
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }

    public List<Tarefa> listar() {
        return tarefaRepository.findByUsuario(getUsuarioLogado());
    }

    public Tarefa adicionar(String descricao) {
        Tarefa tarefa = new Tarefa();
        tarefa.setDescricao(descricao);
        tarefa.setUsuario(getUsuarioLogado());
        return tarefaRepository.save(tarefa);
    }

    public void remover(Long id) {
        Tarefa tarefa = tarefaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tarefa não encontrada"));
        if (!tarefa.getUsuario().getUsername().equals(getUsuarioLogado().getUsername()))
            throw new RuntimeException("Sem permissão para remover esta tarefa");
        tarefaRepository.delete(tarefa);
    }
}
