package com.trier.futmax.dto.response;


public record EstoqueResponseDTO(Long cdEstoque,
                                 String cdLocalEstoque,
                                 Integer qtEstoque,
                                 Boolean flAtivo,
                                 Long cdProduto) {
}
