package com.domus.domus_api.repositories;

import com.domus.domus_api.entities.Multa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MultaRepository extends JpaRepository<Multa, Long> {
    
    // Permite que o morador consulte suas pendências financeiras/punições
    List<Multa> findByInfratorId(Long infratorId);
}