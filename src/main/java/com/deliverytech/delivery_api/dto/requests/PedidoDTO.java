package com.deliverytech.delivery_api.dto.requests;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class PedidoDTO {
    
    @NotBlank(message = "Endereço de entrega é obrigatório")
    private String enderecoEntrega;

    @NotNull(message = "ID do cliente é obrigatório")
    private Long clienteId;

    @NotNull(message = "ID do restaurante é obrigatório")
    private Long restauranteId;

    @Valid
    @NotNull(message = "A lista de itens não pode ser nula")
    @Size(min = 1, message = "O pedido deve ter pelo menos um item")
    private List<com.deliverytech.delivery_api.dto.ItemPedidoDTO> itens;
}
