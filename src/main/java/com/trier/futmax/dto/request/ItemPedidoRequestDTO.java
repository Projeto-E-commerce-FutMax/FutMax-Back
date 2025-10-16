package com.trier.futmax.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ItemPedidoRequestDTO(

        @NotNull(message = "É preciso informar o código do produto!")
        Long cdProduto,

        @NotNull(message = "É preciso informar a quantidade!")
        @Positive(message = "A quantidade deve ser positiva!")
        @Min(value = 1, message = "A quantidade mínima é 1!")
        Integer qtItem
) {}