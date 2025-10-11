package com.trier.futmax.controller;

import com.trier.futmax.dto.response.EstoqueResponseDTO;
import com.trier.futmax.model.EstoqueModel;
import com.trier.futmax.service.EstoqueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/estoque")

public class EstoqueController {

    @Autowired
    private EstoqueService estoqueService;

    @GetMapping
    public ResponseEntity<EstoqueResponseDTO> consultar(@PathVariable Integer cdEstoque) {
        EstoqueResponseDTO estoque = estoqueService.consultarEstoque(cdEstoque);
        return ResponseEntity.status(HttpStatus.OK).body(estoque);
    }
}
