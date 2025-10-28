package com.trier.futmax.dto.response;

public record ProdutoResponseDTO(Long cdProduto,
                                 String nmProduto,
                                 String dsProduto,
                                 Double vlProduto,
                                 Boolean flAtivo,
                                 String imgUrl) {
}
