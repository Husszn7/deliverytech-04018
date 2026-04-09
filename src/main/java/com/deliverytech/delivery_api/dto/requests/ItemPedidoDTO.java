package com.deliverytech.delivery_api.dto.requests;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ItemPedidoDTO {
    @NotNull
    private Long produtoId;

    @NotNull
    @Min(1)
    @Positive
    private Integer quantidade;

}
