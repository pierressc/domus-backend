package com.domus.domus_api.controllers;

import com.domus.domus_api.entities.Usuario;
import com.domus.domus_api.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // CORREÇÃO: Apenas Administrador (N1) ou Síndico (N2) podem criar usuários diretamente por aqui (RF01)
    @PostMapping
    @PreAuthorize("hasAnyAuthority('N1', 'N2')")
    public ResponseEntity<Usuario> cadastrarUsuario(@RequestBody Usuario novoUsuario) {
        
        String senhaCriptografada = passwordEncoder.encode(novoUsuario.getSenha());
        novoUsuario.setSenha(senhaCriptografada);
        
        Usuario usuarioSalvo = usuarioRepository.save(novoUsuario);
        usuarioSalvo.setSenha(null); // Impede a exposição da hash na resposta

        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioSalvo);
    }
}