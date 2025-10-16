package com.trier.futmax.dto.request;

import jakarta.validation.constraints.*;

import java.time.LocalDateTime;

public record PedidoRequestDTO(

        @NotNull(message = "É preciso informar o código de Usuário!")
        Long cdUsuario,

        @NotNull(message = "É preciso informar o valor dos Itens!")
        @Positive(message = "Os valores devem ser positivos!")
        @Min(value = 50, message = "O valor mínimo de pedido é 50!")
        Double vlItens,

        @NotNull(message = "Informe o valor do Frete!")
        @Positive(message = "O valor do frete deve ser positivo!")
        @Min(value = 10, message = "O frete deve obter um valor mínimo de 10!")
        Double vlFrete,

        @NotNull(message = "Informe o valor total do Pedido!")
        @Positive(message = "O valor total do pedido deve ser um número positivo!")
        @Min(value = 100, message = "O valor total do pedido deve ser no mínimo acima de 100!")
        Double vlTotalPedido,

        @NotNull(message = "Informe a data do Pedido!")
        @Future(message = "Informe uma data válida, sem ser datas passadas ou antigas!")
        LocalDateTime dtPedido,

        @NotBlank(message = "Informe se o pedido está ativo ou não!")
        Boolean flAtivo

) {
}
