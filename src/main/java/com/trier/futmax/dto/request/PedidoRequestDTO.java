package com.trier.futmax.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.util.List;

public record PedidoRequestDTO(

        @NotNull(message = "É preciso informar o código de Usuário!")
        Long cdUsuario,

        @NotNull(message = "É preciso informar os itens do pedido!")
        @Size(min = 1, message = "O pedido deve ter pelo menos 1 item!")
        @Valid
        List<ItemPedidoRequestDTO> itens

) {}