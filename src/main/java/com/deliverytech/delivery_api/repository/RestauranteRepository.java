package com.deliverytech.delivery_api.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.deliverytech.delivery_api.enums.CategoriaRestaurante;
import com.deliverytech.delivery_api.model.Restaurante;

@Repository
public interface RestauranteRepository extends JpaRepository<Restaurante, Long> {


        Page<Restaurante> findByAtivoTrue(Pageable pageable);

        
        Page<Restaurante> findByCategoriaAndAtivoTrue(CategoriaRestaurante categoria, Pageable pageable);

        boolean existsByNome(String nome);


        List<Restaurante> findByNomeContainingIgnoreCase(String nome);
        List<Restaurante> findByAtivoTrueOrderByAvaliacaoDesc();
}
