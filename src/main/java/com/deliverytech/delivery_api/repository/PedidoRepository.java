package com.deliverytech.delivery_api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.deliverytech.delivery_api.dto.VendasPorRestauranteDTO;
import com.deliverytech.delivery_api.model.Pedido;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    
    @Query("""
        SELECT 
            r.nome AS nomeRestaurante,
            SUM(p.valorTotal) AS totalVendas
        FROM Pedido p
        JOIN p.restaurante r
        GROUP BY r.nome
    """)
    List<VendasPorRestauranteDTO> buscarVendasPorRestaurante();
}
