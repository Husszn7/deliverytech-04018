package com.deliverytech.delivery_api.dto.requests;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Dados para criação de um novo pedido.")
public class PedidoDTO {

    @Schema(description = "Endereço completo para entrega.", example = "Rua das Flores, 123, Bairro Centro")
    @NotBlank(message = "Endereço de entrega é obrigatório")
    private String enderecoEntrega;

    @Schema(description = "ID do cliente que está realizando o pedido.", example = "1")
    @NotNull(message = "ID do cliente é obrigatório")
    private Long clienteId;

    @Schema(description = "ID do restaurante onde o pedido está sendo feito.", example = "5")
    @NotNull(message = "ID do restaurante é obrigatório")
    private Long restauranteId;

    @Schema(description = "Lista de produtos e quantidades.")
    @Valid
    @NotNull(message = "A lista de itens não pode ser nula")
    @Size(min = 1, message = "O pedido deve ter pelo menos um item")
    private List<ItemPedidoDTO> itens;
 

}