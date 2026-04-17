package com.deliverytech.delivery_api.security;

import java.io.IOException;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.deliverytech.delivery_api.model.Usuario;
import com.deliverytech.delivery_api.repository.UsuarioRepository;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UsuarioRepository repository;

    public JwtAuthenticationFilter(JwtUtil jwtUtil, UsuarioRepository repository) {
        this.jwtUtil = jwtUtil;
        this.repository = repository;
    }

    @Override
    public  void doFilterInternal(
        HttpServletRequest request,
        HttpServletResponse response,
        FilterChain chain
    )throws  IOException, ServletException{
            
                String token = extractToken(request);
                if (token != null) {
                    try {
                        String email = jwtUtil.extractEmail(token);
                        if(email != null && SecurityContextHolder.getContext().getAuthentication() == null){
                            Usuario usuario = repository.findByEmail(email).orElse(null);
                            if (usuario != null && jwtUtil.isTokenValid(token, usuario.getEmail())) {
                                String role = jwtUtil.extractRoles(token);
                                
                                SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + role);

                                UsernamePasswordAuthenticationToken auth =
                                new UsernamePasswordAuthenticationToken(usuario, null, List.of(authority));

                                auth.setDetails(
                                    new WebAuthenticationDetailsSource().buildDetails(request)
                                );

                                SecurityContextHolder.getContext().setAuthentication(auth);
                            }
                        }
                    } catch (Exception e) {
                        System.out.println("Token inválido: " + e.getMessage());
                    }
                }

            
            chain.doFilter(request, response);
    }

    private String extractToken(HttpServletRequest request){
        String authHeader = request.getHeader("Authorization");

        if(authHeader == null || !authHeader.startsWith("Bearer ")){
                return null;
        }
        return authHeader.substring(7);
    }

}
