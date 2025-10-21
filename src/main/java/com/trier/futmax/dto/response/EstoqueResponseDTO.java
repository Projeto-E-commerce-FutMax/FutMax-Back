package com.trier.futmax.dto.response;


public record EstoqueResponseDTO(Long cdEstoque,
                                 Integer qtEstoque,
                                 Boolean flAtivo,
                                 Long cdProduto,
                                 String nmProduto) {
}
