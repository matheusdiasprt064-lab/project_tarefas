package com.tarefas.api.service;

import com.tarefas.api.exception.ConflitoException;
import com.tarefas.api.model.Usuario;
import com.tarefas.api.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository repository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UsuarioService usuarioService;

    @Test
    void registraUsuarioComSenhaCriptografada() {
        when(repository.findByUsername("maria")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("senha123")).thenReturn("hash");
        when(repository.save(any(Usuario.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Usuario usuario = usuarioService.registrar(" maria ", "senha123");

        assertEquals("maria", usuario.getUsername());
        assertEquals("hash", usuario.getPassword());
        verify(repository).save(any(Usuario.class));
    }

    @Test
    void bloqueiaUsernameDuplicado() {
        when(repository.findByUsername("maria")).thenReturn(Optional.of(new Usuario()));

        assertThrows(ConflitoException.class, () -> usuarioService.registrar("maria", "senha123"));
    }
}
