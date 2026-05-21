package com.domus.domus_api.repositories;

import com.domus.domus_api.entities.Unidade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UnidadeRepository extends JpaRepository<Unidade, Long> {
    // O Spring Boot já nos dá todos os comandos de banco de dados aqui!
}