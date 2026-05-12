package com.domus.domus_api.repositories;

import com.domus.domus_api.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    
    // O Spring Boot já cria o código SQL (SELECT * FROM usuarios WHERE email = ?)
    Optional<Usuario> findByEmail(String email);
}