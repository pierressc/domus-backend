package com.domus.domus_api.config;

import com.domus.domus_api.repositories.UsuarioRepository;
import com.domus.domus_api.services.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var token = this.recuperarToken(request);
        
        if (token != null) {
            var login = tokenService.validateToken(token);
            
            if (login != null) {
                // CORREÇÃO: Garante o cumprimento do Soft Delete. Usuários inativos não ganham contexto de autenticação!
                var usuarioOpt = usuarioRepository.findByEmailAndAtivoTrue(login);
                
                if (usuarioOpt.isPresent()) {
                    var usuario = usuarioOpt.get();

                    // Mapeia o perfil limpo (ex: "N2", "N5") diretamente para as autoridades do Spring
                    var authorities = java.util.List.of(new org.springframework.security.core.authority.SimpleGrantedAuthority(usuario.getPerfil()));

                    var authentication = new UsernamePasswordAuthenticationToken(usuario, null, authorities);
                    
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        }
        filterChain.doFilter(request, response);
    }

    private String recuperarToken(HttpServletRequest request) {
        var authHeader = request.getHeader("Authorization");
        if (authHeader == null) return null;
        return authHeader.replace("Bearer ", "");
    }
}