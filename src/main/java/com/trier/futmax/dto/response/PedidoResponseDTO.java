package com.trier.futmax.dto.response;

import java.time.LocalDateTime;

public record PedidoResponseDTO(

        Long cdPedido,
        Long cdUsuario,
        Double vlItens,
        Double vlFrete,
        Double vlTotalPedido,
        LocalDateTime dtPedido,
        Boolean flAtivo

) {
}
