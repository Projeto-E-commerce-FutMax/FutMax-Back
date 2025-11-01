package com.trier.futmax.dto.response;

import java.time.LocalDateTime;
import java.util.List;

public record PedidoResponseDTO(

        Long cdPedido,
        Long cdUsuario,
        String nmUsuario,
        Double vlItens,
        Double vlFrete,
        Double vlTotalPedido,
        LocalDateTime dtPedido,
        Boolean flAtivo,
        List<ItemPedidoResponseDTO> itens

) {

    public PedidoResponseDTO(
            Long cdPedido,
            Long cdUsuario,
            String nmUsuario,
            Double vlItens,
            Double vlFrete,
            Double vlTotalPedido,
            LocalDateTime dtPedido,
            Boolean flAtivo

    ) {
        this(cdPedido, cdUsuario, nmUsuario, vlItens, vlFrete, vlTotalPedido, dtPedido, flAtivo, List.of());
    }
}
