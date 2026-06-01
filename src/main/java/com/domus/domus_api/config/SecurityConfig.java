package com.domus.domus_api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity; // <-- NOVO
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity; // <-- NOVO
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration // Avisa o Spring que esta é uma classe de configuração global
@EnableWebSecurity // Habilita a segurança web do Spring
@EnableMethodSecurity // Habilita o uso do @PreAuthorize nos controllers (A mágica do 403)
public class SecurityConfig {

    @Autowired
    private SecurityFilter securityFilter;

    // Criamos a "máquina de criptografia" (BCrypt é o padrão ouro da indústria)
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Definimos as regras de quem pode acessar o quê
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // Desabilitamos isso pois usaremos tokens JWT
            .authorizeHttpRequests(auth -> auth
                // Liberamos as rotas de Cadastro e Login para qualquer um acessar sem estar logado
                .requestMatchers("/usuarios", "/auth/login").permitAll()
                // Qualquer outra rota futura exigirá que o usuário esteja logado
                .anyRequest().authenticated()
            )
            // ADICIONANDO O FILTRO ANTES DO FILTRO PADRÃO DO SPRING
            .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}