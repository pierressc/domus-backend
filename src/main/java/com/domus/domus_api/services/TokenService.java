package com.domus.domus_api.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.domus.domus_api.entities.Usuario;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
public class TokenService {

    @Value("${api.security.token.secret}")
    private String secret;

    public String gerarToken(Usuario usuario) {
        Algorithm algorithm = Algorithm.HMAC256(secret);
        
        return JWT.create()
                .withIssuer("domus-api")
                .withSubject(usuario.getEmail())
                .withClaim("id", usuario.getId())
                .withClaim("nome", usuario.getNome()) // CORREÇÃO: Alimenta o {{ usuarioNome }} do painel Angular
                .withClaim("perfil", usuario.getPerfil())
                .withExpiresAt(Instant.now().plus(2, ChronoUnit.HOURS))
                .sign(algorithm);
    }

    public String validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                    .withIssuer("domus-api")
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (Exception exception) {
            // CORREÇÃO: Retorna null para o SecurityFilter saber que o token está inválido ou expirado
            return null; 
        }
    }
}