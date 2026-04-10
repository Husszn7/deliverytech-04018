package com.deliverytech.delivery_api.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.deliverytech.delivery_api.model.Produto;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long>{
    List<Produto> findByRestauranteId(Long restauranteId);

    Page<Produto> findByCategoria(String categoria, Pageable pageable);

    List<Produto> findByDisponivelTrue();

    Page<Produto> findByRestauranteIdAndDisponivelTrue(Long restauranteId, Pageable pageable);

}
