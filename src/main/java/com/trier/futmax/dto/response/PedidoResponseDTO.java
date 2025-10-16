package com.trier.futmax.dto.response;

import java.time.LocalDateTime;

public record PedidoResponseDTO(

        Long cdPedido,
        Long cdUsuario,
        String nmUsuario,
        Double vlItens,
        Double vlFrete,
        Double vlTotalPedido,
        LocalDateTime dtPedido,
        Boolean flAtivo

) {
}
