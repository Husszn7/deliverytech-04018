package com.deliverytech.delivery_api.dto.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Schema(description = "Dados de um item específico dentro de um pedido.")
public class ItemPedidoDTO {

    @Schema(description = "Quantidade do produto.", example = "2")
    @NotNull(message = "A quantidade é obrigatória")
    @Min(value = 1, message = "A quantidade mínima é 1")
    @Positive
    private Integer quantidade;

    @Schema(description = "ID do produto desejado.", example = "10")
    @NotNull(message = "O ID do produto é obrigatório")
    private Long produtoId;
}
