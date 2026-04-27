package com.tarefas.api.service;

import com.tarefas.api.model.Usuario;
import com.tarefas.api.repository.UsuarioRepository;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    private final UsuarioRepository repository;
    private final ApplicationContext applicationContext;

    public UsuarioService(UsuarioRepository repository, ApplicationContext applicationContext) {
        this.repository = repository;
        this.applicationContext = applicationContext;
    }

    public Usuario registrar(String username, String password) {
        PasswordEncoder passwordEncoder = applicationContext.getBean(PasswordEncoder.class);
        Usuario usuario = new Usuario();
        usuario.setUsername(username);
        usuario.setPassword(passwordEncoder.encode(password));
        return repository.save(usuario);
    }
}
