package com.trier.futmax.controller;

import com.trier.futmax.dto.request.PedidoRequestDTO;
import com.trier.futmax.dto.response.ItemPedidoResponseDTO;
import com.trier.futmax.dto.response.PedidoResponseDTO;
import com.trier.futmax.service.PedidoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pedidos")
@Tag(name = "Pedido", description = "API para gerenciamento de pedidos")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    @PostMapping("/cadastrar")
    @Operation(
            summary = "Criar um novo pedido",
            description = "Cria um novo pedido com os dados fornecidos e retorna o pedido criado com seu identificador"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Pedido criado com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PedidoResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Dados inválidos fornecidos",
                    content = @Content
            ),
    })
    public ResponseEntity<PedidoResponseDTO> criarPedido(
            @Parameter(description = "Dados do pedido a ser criado", required = true)
            @RequestBody @Valid PedidoRequestDTO pedidoRequest) {

        PedidoResponseDTO pedido = pedidoService.criarPedido(pedidoRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(pedido);
    }

    @GetMapping("/buscar/{cdPedido}")
    @Operation(
            summary = "Buscar pedido por código",
            description = "Retorna os detalhes de um pedido específico através do seu código identificador"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Pedido encontrado com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PedidoResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Pedido não encontrado",
                    content = @Content
            ),
    })
    public ResponseEntity<PedidoResponseDTO> buscarPedido(
            @Parameter(description = "Código do pedido", required = true, example = "1")
            @PathVariable Long cdPedido) {

        PedidoResponseDTO pedido = pedidoService.buscarPedido(cdPedido);
        return ResponseEntity.ok(pedido);
    }

    @GetMapping("/{cdPedido}/itens")
    @Operation(
            summary = "Buscar itens de um pedido",
            description = "Retorna a lista de todos os itens associados a um pedido específico"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Itens do pedido retornados com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ItemPedidoResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Pedido não encontrado",
                    content = @Content
            ),
    })
    public ResponseEntity<List<ItemPedidoResponseDTO>> buscarItensPedido(
            @Parameter(description = "Código do pedido", required = true, example = "1")
            @PathVariable Long cdPedido) {

        List<ItemPedidoResponseDTO> itens = pedidoService.buscarItensPedido(cdPedido);
        return ResponseEntity.ok(itens);
    }

    @GetMapping("/buscar/todos")
    @Operation(
            summary = "Listar todos os pedidos",
            description = "Retorna uma lista com todos os pedidos cadastrados no sistema"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista de pedidos retornada com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PedidoResponseDTO.class)
                    )
            ),
    })
    public ResponseEntity<List<PedidoResponseDTO>> buscarTodos() {
        List<PedidoResponseDTO> pedidos = pedidoService.buscarTodos();
        return ResponseEntity.ok(pedidos);
    }
}