package com.tarefas.api.repository;

import com.tarefas.api.model.Tarefa;
import com.tarefas.api.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TarefaRepository extends JpaRepository<Tarefa, Long> {
    List<Tarefa> findByUsuario(Usuario usuario);
}
