package com.deliverytech.delivery_api.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.deliverytech.delivery_api.dto.VendasPorRestauranteDTO;
import com.deliverytech.delivery_api.enums.StatusPedido;
import com.deliverytech.delivery_api.model.Pedido;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {


    @Query(value = """
        SELECT DISTINCT p FROM Pedido p
        JOIN FETCH p.cliente
        JOIN FETCH p.restaurante
        LEFT JOIN FETCH p.itens i
        LEFT JOIN FETCH i.produto
        WHERE p.cliente.id = :clienteId
        """, 
        countQuery = "SELECT count(p) FROM Pedido p WHERE p.cliente.id = :clienteId")
    Page<Pedido> buscarItensPorClientes(@Param("clienteId") Long clienteId, Pageable pageable);

    List<Pedido> findByStatus(StatusPedido status);

    @Query("""
            SELECT p FROM Pedido p
            WHERE p.dataPedido  BETWEEN :inicio AND :fim
    """)
    List<Pedido> findByDateTime(
        @Param("inicio") LocalDateTime inicio,
        @Param("fim") LocalDateTime fim
    );


        /* @Query("""
            select new com.deliverytech.delivery_api.dto.TotalVendasPorRestauranteDTO(
                    r.nome,
                    coalesce(sum(ip.subtotal), 0)
                )
                from Pedido p
                join p.restaurante r
                join p.itens ip
                group by r.nome
            """)
            List<VendasPorRestauranteDTO> buscarVendasPorRestaurante(); */

            @Query(value="""
                        SELECT c.nome AS cliente, COUNT(p.id) AS total_pedidos
                        FROM pedidos p 
                        JOIN clientes c ON c.id = p.cliente_id
                        GROUP BY c.nome
                        ORDER BY total_pedidos DESC
                """, nativeQuery = true )
            List<Object[]> rankingClientes();
    
}

