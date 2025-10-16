package com.trier.futmax.controller;

import com.trier.futmax.dto.request.EstoqueRequestDTO;
import com.trier.futmax.dto.response.EstoqueResponseDTO;
import com.trier.futmax.model.EstoqueModel;
import com.trier.futmax.service.EstoqueService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/estoque")

public class EstoqueController {

    @Autowired
    private EstoqueService estoqueService;

    @PostMapping("/cadastrar")
    public ResponseEntity<EstoqueResponseDTO> cadastrarEstoque(@RequestBody @Valid EstoqueRequestDTO estoqueRequest) {
        var estoque = estoqueService.cadastrarEstoque(estoqueRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(estoque);
    }

    @GetMapping("/buscar/{cdEstoque}")
    public ResponseEntity<EstoqueResponseDTO> consultar(@PathVariable Long cdEstoque) {
        EstoqueResponseDTO estoque = estoqueService.consultarEstoque(cdEstoque);
        return ResponseEntity.status(HttpStatus.OK).body(estoque);
    }

    @GetMapping("/buscar/todos")
    public ResponseEntity<List<EstoqueModel>> consultarTodos() {
        var estoque = estoqueService.consultarTodos();
        return ResponseEntity.status(HttpStatus.OK).body(estoque);
    }

    @PutMapping("/atualizar/{cdEstoque}")
    public ResponseEntity<EstoqueResponseDTO> atualizarEstoque(@PathVariable Long cdEstoque,
                                                               @RequestBody @Valid EstoqueRequestDTO estoqueRequest) {
        EstoqueResponseDTO estoque = estoqueService.atualizarEstoque(cdEstoque, estoqueRequest);
        return ResponseEntity.status(HttpStatus.OK).body(estoque);
    }

    @DeleteMapping("/remover/{cdEstoque}")
    public ResponseEntity<EstoqueResponseDTO> removerEstoque(@PathVariable Long cdEstoque) {
        estoqueService.removerEstoque(cdEstoque);
        return ResponseEntity.noContent().build();
    }
}
