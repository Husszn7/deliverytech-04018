package com.deliverytech.delivery_api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.deliverytech.delivery_api.security.JwtAuthenticationFilter;

import jakarta.servlet.http.HttpServletResponse;

@EnableMethodSecurity
@Configuration
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http

            .csrf(csrf -> csrf.disable())

            .sessionManagement(sm ->
                sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )


            .exceptionHandling(ex -> ex
                .authenticationEntryPoint((req, res, e) ->
                    res.sendError(HttpServletResponse.SC_UNAUTHORIZED)
                )
            )

            .authorizeHttpRequests(auth -> auth

                .requestMatchers("/api/auth/login", "/api/auth/register").permitAll()
                .requestMatchers("/api/auth/me").authenticated()
                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()

                .requestMatchers(HttpMethod.GET, "/api/restaurantes/**").permitAll()
                .requestMatchers(HttpMethod.PATCH, "/api/restaurantes/**")
                    .hasAnyRole("ADMIN", "RESTAURANTE")

                .requestMatchers(HttpMethod.GET, "/api/clientes/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.POST,"/api/clientes/cadastrar").hasAnyRole("ADMIN", "CLIENTE")

                .requestMatchers("/api/pedidos/**").hasAnyRole("ADMIN", "CLIENTE")

                .requestMatchers(HttpMethod.POST, "/api/produtos/**")
                .hasAnyRole("ADMIN", "RESTAURANTE")

                .anyRequest().authenticated()
            )

            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}