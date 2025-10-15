package com.trier.futmax.dto.response;

import com.trier.futmax.model.ProdutoModel;

public record EstoqueResponseDTO(Integer cdEstoque,
                                 String cdLocalEstoque,
                                 Integer qtEstoque,
                                 Boolean flAtivo,
                                 Long cdProduto) {
}
