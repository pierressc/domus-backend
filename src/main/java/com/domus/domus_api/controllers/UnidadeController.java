package com.domus.domus_api.controllers;

import com.domus.domus_api.entities.Unidade;
import com.domus.domus_api.repositories.UnidadeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/unidades")
public class UnidadeController {

    @Autowired
    private UnidadeRepository unidadeRepository;

    // Rota para CADASTRAR a unidade (RF04)
    @PostMapping
    @PreAuthorize("hasAuthority('N2')")
    public ResponseEntity<Unidade> cadastrarUnidade(@RequestBody Unidade novaUnidade) {
        Unidade unidadeSalva = unidadeRepository.save(novaUnidade);
        return ResponseEntity.status(HttpStatus.CREATED).body(unidadeSalva);
    }

    // Rota para o Front: LISTAR todas as unidades cadastradas
    @GetMapping
    public ResponseEntity<List<Unidade>> listarUnidades() {
        return ResponseEntity.ok(unidadeRepository.findAll());
    }
}