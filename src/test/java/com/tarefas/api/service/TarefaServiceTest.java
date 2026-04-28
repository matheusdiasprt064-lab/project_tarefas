package com.tarefas.api.service;

import com.tarefas.api.dto.TarefaRequestDTO;
import com.tarefas.api.dto.TarefaResponseDTO;
import com.tarefas.api.exception.AcessoNegadoException;
import com.tarefas.api.model.Tarefa;
import com.tarefas.api.model.Usuario;
import com.tarefas.api.repository.TarefaRepository;
import com.tarefas.api.repository.UsuarioRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TarefaServiceTest {

    @Mock
    private TarefaRepository tarefaRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private TarefaService tarefaService;

    private Usuario usuario;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setUsername("maria");
        SecurityContextHolder.getContext().setAuthentication(
            new UsernamePasswordAuthenticationToken("maria", null)
        );
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void adicionaTarefaParaUsuarioLogado() {
        TarefaRequestDTO dto = new TarefaRequestDTO();
        dto.setTitulo(" Estudos ");
        dto.setDescricao(" Revisar API ");
        dto.setConcluida(true);
        dto.setDataVencimento(LocalDate.of(2026, 5, 10));

        when(usuarioRepository.findByUsername("maria")).thenReturn(Optional.of(usuario));
        when(tarefaRepository.save(any(Tarefa.class))).thenAnswer(invocation -> invocation.getArgument(0));

        TarefaResponseDTO resposta = tarefaService.adicionar(dto);

        assertEquals("Estudos", resposta.getTitulo());
        assertEquals("Revisar API", resposta.getDescricao());
        assertEquals("maria", resposta.getUsuario());
    }

    @Test
    void bloqueiaAtualizacaoDeTarefaDeOutroUsuario() {
        Usuario outroUsuario = new Usuario();
        outroUsuario.setUsername("joao");
        Tarefa tarefa = new Tarefa();
        tarefa.setUsuario(outroUsuario);
        tarefa.setDescricao("Outra tarefa");

        TarefaRequestDTO dto = new TarefaRequestDTO();
        dto.setDescricao("Nova descricao");

        when(tarefaRepository.findById(1L)).thenReturn(Optional.of(tarefa));
        when(usuarioRepository.findByUsername("maria")).thenReturn(Optional.of(usuario));

        assertThrows(AcessoNegadoException.class, () -> tarefaService.atualizar(1L, dto));
    }
}
