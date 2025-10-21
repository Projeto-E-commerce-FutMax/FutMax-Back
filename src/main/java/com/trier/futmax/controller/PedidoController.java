package com.trier.futmax.controller;

import com.trier.futmax.dto.request.PedidoRequestDTO;
import com.trier.futmax.dto.response.ItemPedidoResponseDTO;
import com.trier.futmax.dto.response.PedidoResponseDTO;
import com.trier.futmax.service.PedidoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    @PostMapping("/cadastrar")
    public ResponseEntity<PedidoResponseDTO> criarPedido(
            @RequestBody @Valid PedidoRequestDTO pedidoRequest) {

        PedidoResponseDTO pedido = pedidoService.criarPedido(pedidoRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(pedido);
    }

    @GetMapping("/buscar/{cdPedido}")
    public ResponseEntity<PedidoResponseDTO> buscarPedido(@PathVariable Long cdPedido) {
        PedidoResponseDTO pedido = pedidoService.buscarPedido(cdPedido);
        return ResponseEntity.ok(pedido);
    }

    @GetMapping("/{cdPedido}/itens")
    public ResponseEntity<List<ItemPedidoResponseDTO>> buscarItensPedido(
            @PathVariable Long cdPedido) {

        List<ItemPedidoResponseDTO> itens = pedidoService.buscarItensPedido(cdPedido);
        return ResponseEntity.ok(itens);
    }

    @GetMapping("/buscar/todos")
    public ResponseEntity<List<PedidoResponseDTO>> buscarTodos() {
        List<PedidoResponseDTO> pedidos = pedidoService.buscarTodos();
        return ResponseEntity.ok(pedidos);
    }
}