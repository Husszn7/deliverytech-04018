package com.deliverytech.delivery_api.dto.responses;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ProdutoResponseDTO {
    private Long id;
    private String nome;

    private String descricao;

    private String categoria;

    private BigDecimal preco;

    private boolean disponivel;

    private Long restauranteId;
}
