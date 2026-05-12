package com.domus.domus_api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration // Avisa o Spring que esta é uma classe de configuração global
public class SecurityConfig {

    // 1. Criamos a "máquina de criptografia" (BCrypt é o padrão ouro da indústria)
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 2. Definimos as regras de quem pode acessar o quê
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // Desabilitamos isso pois usaremos tokens JWT
            .authorizeHttpRequests(auth -> auth
                // Liberamos as rotas de Cadastro e Login para qualquer um acessar sem estar logado
                .requestMatchers("/usuarios", "/auth/login").permitAll()
                // Qualquer outra rota futura exigirá que o usuário esteja logado
                .anyRequest().authenticated()
            );
        return http.build();
    }
}