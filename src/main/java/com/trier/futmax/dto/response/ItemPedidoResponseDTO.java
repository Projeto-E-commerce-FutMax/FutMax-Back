package com.trier.futmax.dto.response;

public record ItemPedidoResponseDTO(
        Long cdItemPedido,
        Long cdPedido,
        Long cdProduto,
        String nmProduto,
        Integer qtItem,
        Double vlUnitario,
        Double vlTotal,
        Boolean flAtivo
) {}