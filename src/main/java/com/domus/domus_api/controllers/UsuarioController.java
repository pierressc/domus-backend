package com.domus.domus_api.controllers;

import com.domus.domus_api.entities.Usuario;
import com.domus.domus_api.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired // Injetamos a máquina de criptografia aqui!
    private PasswordEncoder passwordEncoder;

    @PostMapping
    public ResponseEntity<Usuario> cadastrarUsuario(@RequestBody Usuario novoUsuario) {
        
        // Criptografamos a senha antes de salvar (Cumprindo o RNF02 da LGPD)
        String senhaCriptografada = passwordEncoder.encode(novoUsuario.getSenha());
        novoUsuario.setSenha(senhaCriptografada);
        
        // Agora sim, salvamos no banco
        Usuario usuarioSalvo = usuarioRepository.save(novoUsuario);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioSalvo);
    }
}