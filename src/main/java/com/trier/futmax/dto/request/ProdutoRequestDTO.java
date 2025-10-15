package com.trier.futmax.dto.request;

import jakarta.validation.constraints.*;

public record ProdutoRequestDTO(
        @NotBlank (message = "O nome do produto é obrigatório")
        @Size (max = 100, message = "O nome do produto deve ter no máximo 100 caracteres")
        String nmProduto,

        @NotNull (message = "O preço do produto é obrigatório")
        @Positive (message = "O preço do produto deve ser positivo")
        Double vlProduto,

        @NotNull (message = "A descrição do produto é obrigatória")
        @Size (max = 255, message = "A descrição do produto deve ter no máximo 255 caracteres")
        String dsProduto,

        @NotBlank (message = "O campo ativo é obrigatório")
        Boolean flAtivo
) {



}
