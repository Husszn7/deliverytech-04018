package com.deliverytech.delivery_api.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.deliverytech.delivery_api.model.Cliente;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    Optional<Cliente> findByEmail(String email);
    boolean existsByEmail(String email);
    Page<Cliente> findByAtivoTrue(Pageable pageable);
    List<Cliente> findByNomeContainingIgnoreCase(String nome);

    boolean existsByUsuario_Id(Long usuarioId);
    
}


