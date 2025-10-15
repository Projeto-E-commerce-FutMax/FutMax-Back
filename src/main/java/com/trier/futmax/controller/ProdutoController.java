package com.trier.futmax.controller;


import com.trier.futmax.dto.request.ProdutoRequestDTO;
import com.trier.futmax.dto.response.ProdutoResponseDTO;
import com.trier.futmax.model.ProdutoModel;
import com.trier.futmax.service.ProdutoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/produto")

public class ProdutoController {

    @Autowired
    private ProdutoService produtoService;

    @PostMapping("/cadastrar")
    public ResponseEntity<ProdutoResponseDTO> cadastrarProduto(@RequestBody @Valid ProdutoRequestDTO produtoRequest) {
        var produto = produtoService.cadastrarProduto(produtoRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(produto);
    }

    @GetMapping("/buscar")
    public ResponseEntity<ProdutoResponseDTO> consultar(@RequestParam Long cdProduto) {
        ProdutoResponseDTO produto = produtoService.consultarProduto(cdProduto);
        return ResponseEntity.status(HttpStatus.OK).body(produto);
    }

    @GetMapping("/consultarTodos")
    public ResponseEntity<List<ProdutoModel>> consultarTodos() {
        var produto = produtoService.consultarTodos();
        return ResponseEntity.status(HttpStatus.OK).body(produto);
    }

    @PutMapping("/atualizar")
    public ResponseEntity<ProdutoResponseDTO> atualizarProduto(@RequestBody @Valid ProdutoRequestDTO produtoRequest) {
        ProdutoResponseDTO produto = produtoService.atualizarProduto(produtoRequest);
        return ResponseEntity.status(HttpStatus.OK).body(produto);
    }
    @DeleteMapping("Desativar")
    public ResponseEntity<ProdutoResponseDTO> desativarProduto(@RequestParam Long cdProduto) {
        produtoService.desativarProduto(cdProduto);
        return ResponseEntity.noContent().build();
    }
}

