package com.deliverytech.delivery_api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.deliverytech.delivery_api.dto.ItemPedidoDTO;
import com.deliverytech.delivery_api.model.ItemPedido;

import org.springframework.stereotype.Repository;

@Repository
public interface ItemPedidoRepository extends JpaRepository<ItemPedido, Long>{

    @Query("""
            SELECT 
                p.nome AS nomeProduto,
                i.quantidade AS quantidade,
                i.subtotal AS subtotal
            FROM ItemPedido i
            JOIN i.produto p
            WHERE i.pedido.id = :pedidoId
    """)
    List<ItemPedidoDTO> buscarItensPorPedido(@Param("pedidoId") Long pedidoId);
}
