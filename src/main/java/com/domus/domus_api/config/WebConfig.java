package com.domus.domus_api.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // Libera todas as suas rotas (/auth, /multas, etc)
                .allowedOriginPatterns("*") // Libera qualquer frontend
                .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS") // Libera os verbos
                .allowedHeaders("*") // Libera o envio do header de Authorization
                .allowCredentials(true);
    }
}