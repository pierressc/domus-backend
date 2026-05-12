package com.domus.domus_api.controllers;

import com.domus.domus_api.entities.Usuario;
import com.domus.domus_api.repositories.UsuarioRepository;
import com.domus.domus_api.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired private UsuarioRepository usuarioRepository;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Map<String, String> credenciais) {
        String email = credenciais.get("email");
        String senha = credenciais.get("senha");

        // 1. Busca o usuário pelo e-mail
        var usuarioOptional = usuarioRepository.findByEmail(email);

        if (usuarioOptional.isPresent()) {
            Usuario usuario = usuarioOptional.get();
            
            // 2. Compara a senha digitada com a senha criptografada do banco
            if (passwordEncoder.matches(senha, usuario.getSenha())) {
                // 3. Senha correta! Gera o token e devolve pro Frontend
                String token = tokenService.gerarToken(usuario);
                return ResponseEntity.ok(token);
            }
        }
        
        // Se o e-mail não existir ou a senha estiver errada, retorna erro 401 (Não Autorizado)
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciais inválidas!");
    }
}