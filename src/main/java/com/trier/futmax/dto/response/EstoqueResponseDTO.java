package com.trier.futmax.dto.response;


public record EstoqueResponseDTO(Integer cdEstoque,
                                 String cdLocalEstoque,
                                 Integer qtEstoque,
                                 Boolean flAtivo,
                                 Long cdProduto) {
}
