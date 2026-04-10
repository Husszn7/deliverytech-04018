package com.deliverytech.delivery_api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.deliverytech.delivery_api.dto.ipDTO;
import com.deliverytech.delivery_api.model.ItemPedido;

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
    List<ipDTO> buscarItensPorPedido(@Param("pedidoId") Long pedidoId);
}
