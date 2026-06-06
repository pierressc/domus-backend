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

        var usuarioOptional = usuarioRepository.findByEmail(email);

        if (usuarioOptional.isPresent()) {
            Usuario usuario = usuarioOptional.get();
            
            if (passwordEncoder.matches(senha, usuario.getSenha())) {
                String token = tokenService.gerarToken(usuario);
                return ResponseEntity.ok(token);
            }
        }
        
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciais inválidas!");
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody Usuario novoUsuario) {
        
        if (usuarioRepository.findByEmail(novoUsuario.getEmail()).isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("E-mail já cadastrado!");
        }

        String senhaCriptografada = passwordEncoder.encode(novoUsuario.getSenha());
        novoUsuario.setSenha(senhaCriptografada);

        // CORREÇÃO: Alinhando com os níveis N1 a N5 exigidos pela Matriz de Permissões
        if (novoUsuario.getPerfil() == null || novoUsuario.getPerfil().isEmpty()) {
            novoUsuario.setPerfil("N5"); // Define como Morador padrão por segurança
        }

        usuarioRepository.save(novoUsuario);
        return ResponseEntity.status(HttpStatus.CREATED).body("Usuário cadastrado com sucesso!");
    }
}