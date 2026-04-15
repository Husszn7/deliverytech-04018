package com.deliverytech.delivery_api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.deliverytech.delivery_api.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmail(String email);
    boolean existsByEmail(String email);
}
