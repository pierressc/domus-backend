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
    private TokenService tokenService; // Serviço que você criou na Sprint 1

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var token = this.recuperarToken(request);
        
        if (token != null) {
            // Valida o token e extrai o e-mail/login (ajuste o método validateToken se necessário)
            var login = tokenService.validateToken(token);
            
            // Busca o usuário no banco (agora recebendo o Optional)
            var usuarioOpt = usuarioRepository.findByEmail(login);
            
            // Verificamos se a "caixa" não está vazia
            if (usuarioOpt.isPresent()) {
                // Extraímos o usuário de dentro do Optional
                var usuario = usuarioOpt.get();

                // Pegamos o perfil do seu usuário (ex: "SINDICO" ou "MORADOR")
                // e transformamos no formato de permissão que o Spring Security entende
                var authorities = java.util.List.of(new org.springframework.security.core.authority.SimpleGrantedAuthority(usuario.getPerfil()));

                // Cria o objeto de autenticação passando as permissões criadas acima
                var authentication = new UsernamePasswordAuthenticationToken(usuario, null, authorities);
                
                // Salva a autenticação no contexto do Spring
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        filterChain.doFilter(request, response); // Continua o fluxo da requisição
    }

    private String recuperarToken(HttpServletRequest request) {
        var authHeader = request.getHeader("Authorization");
        if (authHeader == null) return null;
        return authHeader.replace("Bearer ", ""); // Remove o prefixo padrão
    }
}