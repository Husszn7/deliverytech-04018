package com.deliverytech.delivery_api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.deliverytech.delivery_api.dto.requests.LoginRequestDTO;
import com.deliverytech.delivery_api.enums.Role;
import com.deliverytech.delivery_api.model.Usuario;
import com.deliverytech.delivery_api.repository.UsuarioRepository;
import com.deliverytech.delivery_api.security.JwtUtil;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

@RestController
@RequestMapping("/api/auth")
public class AutenticacaoController {
    private final UsuarioRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AutenticacaoController(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder,
            JwtUtil jwtUtil) {
        this.repository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public ResponseEntity<?> cadastrar(@RequestBody LoginRequestDTO request){
        if(repository.existsByEmail(request.getEmail())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("E-mail já cadastrado.");
        }

        if(request.getRole() == Role.ADMIN){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Não é permitido criar usuários com função ADMIN.");
        }

        Usuario usuario = new Usuario();
        usuario.setEmail(request.getEmail());
        usuario.setSenha(passwordEncoder.encode(request.getSenha()));
        usuario.setRole(request.getRole());
        repository.save(usuario);

        return ResponseEntity.status(HttpStatus.CREATED).body("Usuário cadastrado com sucesso.");
    }

}
