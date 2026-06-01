package com.domus.domus_api.controllers;

import com.domus.domus_api.entities.Ocorrencia;
import com.domus.domus_api.repositories.OcorrenciaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ocorrencias")
public class OcorrenciaController {

    @Autowired
    private OcorrenciaRepository ocorrenciaRepository;

    // Apenas moradores podem registrar incidentes. Outros recebem 403.
    @PostMapping
    @PreAuthorize("hasAuthority('MORADOR')")
    public ResponseEntity<Ocorrencia> registrarOcorrencia(@RequestBody Ocorrencia novaOcorrencia) {
        Ocorrencia ocorrenciaSalva = ocorrenciaRepository.save(novaOcorrencia);
        return ResponseEntity.status(HttpStatus.CREATED).body(ocorrenciaSalva);
    }
}