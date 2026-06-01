package com.domus.domus_api.controllers;

import com.domus.domus_api.entities.Multa;
import com.domus.domus_api.repositories.MultaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/multas")
public class MultaController {

    @Autowired
    private MultaRepository multaRepository;

    // Apenas o Síndico pode aplicar multas. Qualquer outro recebe 403.
    @PostMapping
    @PreAuthorize("hasAuthority('N2')")
    public ResponseEntity<Multa> aplicarMulta(@RequestBody Multa novaMulta) {
        Multa multaSalva = multaRepository.save(novaMulta);
        return ResponseEntity.status(HttpStatus.CREATED).body(multaSalva);
    }
}