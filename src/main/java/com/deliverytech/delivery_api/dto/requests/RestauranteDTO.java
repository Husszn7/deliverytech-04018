package com.deliverytech.delivery_api.dto.requests;

import java.math.BigDecimal;

import com.deliverytech.delivery_api.validation.CategoriaValida;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RestauranteDTO {


    @NotBlank(message = "Nome do restaurante é obrigatório")
    private String nome;

    @NotBlank(message = "Categoria é obrigatória")
    @CategoriaValida
    private String categoria;

    @Size(min = 5, max = 255, message = "Endereço deve ter entre 5 e 255 caracteres")
    private String endereco;

    @NotBlank(message = "Telefone é obrigatório")
    @Pattern(
        regexp = "\\(?\\d{2}\\)?[\\s-]?\\d{4,5}-?\\d{4}",
        message = "Telefone inválido. Formato esperado: (XX) XXXXX-XXXX ou similar"
    )
    private String telefone;

    @NotNull(message = "A taxa de entrega é obrigatória")
    private BigDecimal taxaEntrega;
}
