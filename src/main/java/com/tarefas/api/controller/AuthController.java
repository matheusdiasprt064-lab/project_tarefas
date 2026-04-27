package com.tarefas.api.controller;

import com.tarefas.api.repository.UsuarioRepository;
import com.tarefas.api.security.JwtUtil;
import com.tarefas.api.service.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
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
    public ResponseEntity<?> register(@RequestBody Map<String, String> body) {
        usuarioService.registrar(body.get("username"), body.get("password"));
        return ResponseEntity.ok("Usuário registrado com sucesso");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
        var usuario = usuarioRepository.findByUsername(body.get("username"))
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        if (!passwordEncoder.matches(body.get("password"), usuario.getPassword()))
            return ResponseEntity.status(401).body("Senha incorreta");
        String token = jwtUtil.generateToken(usuario.getUsername());
        return ResponseEntity.ok(Map.of("token", token));
    }
}
