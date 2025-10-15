package com.trier.futmax.dto.request;

import com.trier.futmax.model.ProdutoModel;
import jakarta.validation.constraints.*;

public record EstoqueRequestDTO(@NotBlank(message = "É preciso informar o local do estoque!")
                         String cdLocalEstoque,
                                @NotNull(message = "É preciso informar a quantidade que será adicionada ao estoque!")
                         Integer qtEstoque,
                                @NotNull(message = "É preciso informar o código do produto!")
                         Long cdProduto) {
}
