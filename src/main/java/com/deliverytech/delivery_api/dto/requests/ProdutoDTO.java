package com.deliverytech.delivery_api.dto.requests;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProdutoDTO {

    @NotBlank(message = "Nome é obrigatório")
    private String nome;

    @NotBlank(message = "Descrição é obrigatória")
    @Size(min = 5, message = "Descrição deve ter ao menos 5 caracteres")
    private String descricao;

    @NotBlank(message = "Categoria é obrigatória")
    private String categoria;


    @Positive(message = "O preço deve ser maior que zero")
    @NotNull(message = "Preço é obrigatório")
    private BigDecimal preco;
}
