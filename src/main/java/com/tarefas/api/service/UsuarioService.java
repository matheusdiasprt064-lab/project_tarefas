package com.tarefas.api.service;

import com.tarefas.api.exception.ConflitoException;
import com.tarefas.api.model.Usuario;
import com.tarefas.api.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    private final UsuarioRepository repository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    public Usuario registrar(String username, String password) {
        String usernameNormalizado = username.trim();
        if (repository.findByUsername(usernameNormalizado).isPresent()) {
            throw new ConflitoException("Username ja cadastrado");
        }
        Usuario usuario = new Usuario();
        usuario.setUsername(usernameNormalizado);
        usuario.setPassword(passwordEncoder.encode(password));
        return repository.save(usuario);
    }
}
