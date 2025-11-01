package com.trier.futmax.dto.request;

import jakarta.validation.constraints.*;

public record EstoqueRequestDTO(

        Integer qtEstoque,

        @NotNull(message = "É preciso informar o código do produto!")
        Long cdProduto) {
}
