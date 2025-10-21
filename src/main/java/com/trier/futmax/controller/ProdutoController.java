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

    @GetMapping("/buscar/{cdProduto}")
    public ResponseEntity<ProdutoResponseDTO> consultar(@PathVariable Long cdProduto) {
        ProdutoResponseDTO produto = produtoService.consultarProduto(cdProduto);
        return ResponseEntity.status(HttpStatus.OK).body(produto);
    }

    @GetMapping("/buscar/todos")
    public ResponseEntity<List<ProdutoResponseDTO>> buscarTodos() {
        var produto = produtoService.buscarTodos();
        return ResponseEntity.status(HttpStatus.OK).body(produto);
    }

    @PutMapping("/atualizar/{cdProduto}")
    public ResponseEntity<ProdutoResponseDTO> atualizarProduto(@PathVariable Long cdProduto,
            @RequestBody @Valid ProdutoRequestDTO produtoRequestDTO
    ) {
        ProdutoResponseDTO produto = produtoService.atualizarProduto(cdProduto, produtoRequestDTO);
        return ResponseEntity.ok(produto);
    }

    @DeleteMapping("/desativar/{cdProduto}")
    public ResponseEntity<ProdutoResponseDTO> desativarProduto(@PathVariable Long cdProduto) {
        var produto = produtoService.desativarProduto(cdProduto);
        return ResponseEntity.status(HttpStatus.OK).body(produto);    }

    
    @PutMapping("/reativar/{cdProduto}")
    public ResponseEntity<ProdutoResponseDTO> reativarProduto(@PathVariable Long cdProduto) {
        var produto = produtoService.reativarProduto(cdProduto);
        return ResponseEntity.status(HttpStatus.OK).body(produto);

    }

}

