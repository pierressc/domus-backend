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

    // O Spring vai no application.properties e injeta o valor aqui dentro!
    @Value("${api.security.token.secret}")
    private String secret;

    public String gerarToken(Usuario usuario) {
        Algorithm algorithm = Algorithm.HMAC256(secret);
        
        return JWT.create()
                .withIssuer("domus-api")
                .withSubject(usuario.getEmail())
                .withClaim("id", usuario.getId())
                .withClaim("perfil", usuario.getPerfil())
                .withExpiresAt(Instant.now().plus(2, ChronoUnit.HOURS))
                .sign(algorithm);
    }

    // Método para ler o token, validar a assinatura e extrair o e-mail (subject)
    public String validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                    .withIssuer("domus-api")
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (Exception exception) {
            // Se o token for inválido, expirado ou adulterado, retorna vazio
            return ""; 
        }
    }
}