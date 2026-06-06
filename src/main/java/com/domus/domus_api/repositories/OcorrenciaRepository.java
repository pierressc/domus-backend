package com.domus.domus_api.repositories;

import com.domus.domus_api.entities.Ocorrencia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface OcorrenciaRepository extends JpaRepository<Ocorrencia, Long> {
    
    // RF07: Permite buscar as ocorrências abertas por um morador específico
    List<Ocorrencia> findByMoradorId(Long moradorId);
}