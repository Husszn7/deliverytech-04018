package com.deliverytech.delivery_api.dto.responses;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.deliverytech.delivery_api.enums.StatusPedido;
import com.deliverytech.delivery_api.model.ItemPedido;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PedidoResponseDTO {
    private Long id;
    private LocalDateTime dataPedido;
    private BigDecimal valorTotal;
    private StatusPedido status;
    private String enderecoEntrega;
    private List<ItemPedido> itens;

    private String nomeCliente;
    private String nomeRestaurante;
}

