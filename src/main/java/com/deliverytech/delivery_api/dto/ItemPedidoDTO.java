package com.deliverytech.delivery_api.dto;

import java.math.BigDecimal;

public interface ItemPedidoDTO {
    String getNomeProduto();
    Integer getQuantidade();
    BigDecimal getSubtotal();
}
