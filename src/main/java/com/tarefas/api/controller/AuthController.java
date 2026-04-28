package com.tarefas.api.controller;

import com.tarefas.api.dto.AuthRequestDTO;
import com.tarefas.api.dto.AuthResponseDTO;
import com.tarefas.api.repository.UsuarioRepository;
import com.tarefas.api.security.JwtUtil;
import com.tarefas.api.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@Tag(name = "Autenticacao")
public class AuthController {

    private final UsuarioService usuarioService;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthController(UsuarioService usuarioService, UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.usuarioService = usuarioService;
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/register")
    @Operation(summary = "Registrar novo usuario")
    public ResponseEntity<String> register(@Valid @RequestBody AuthRequestDTO body) {
        usuarioService.registrar(body.getUsername(), body.getPassword());
        return ResponseEntity.ok("Usuario registrado com sucesso");
    }

    @PostMapping("/login")
    @Operation(summary = "Login e geracao de token JWT")
    public ResponseEntity<?> login(@Valid @RequestBody AuthRequestDTO body) {
        var usuario = usuarioRepository.findByUsername(body.getUsername().trim());
        if (usuario.isEmpty() || !passwordEncoder.matches(body.getPassword(), usuario.get().getPassword())) {
            return ResponseEntity.status(401).body("Usuario ou senha invalidos");
        }
        String token = jwtUtil.generateToken(usuario.get().getUsername());
        return ResponseEntity.ok(new AuthResponseDTO(token));
    }
}
