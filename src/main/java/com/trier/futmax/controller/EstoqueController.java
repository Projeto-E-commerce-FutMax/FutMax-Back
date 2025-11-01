package com.trier.futmax.controller;

import com.trier.futmax.dto.request.EstoqueRequestDTO;
import com.trier.futmax.dto.response.EstoqueResponseDTO;
import com.trier.futmax.service.EstoqueService;
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
@RequestMapping("/api/estoque")
@Tag(name = "Estoque", description = "API para gerenciamento de estoque")
public class EstoqueController {

    @Autowired
    private EstoqueService estoqueService;

    @PostMapping("/cadastrar")
    @Operation(
            summary = "Cadastrar um novo estoque",
            description = "Cria um novo registro de estoque com os dados fornecidos e retorna o estoque criado com seu identificador"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Estoque cadastrado com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = EstoqueResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Dados inválidos fornecidos",
                    content = @Content
            )
    })
    public ResponseEntity<EstoqueResponseDTO> cadastrarEstoque(
            @Parameter(description = "Dados do estoque a ser cadastrado", required = true)
            @RequestBody @Valid EstoqueRequestDTO estoqueRequest) {

        var estoque = estoqueService.cadastrarEstoque(estoqueRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(estoque);
    }

    @GetMapping("/buscar/{cdEstoque}")
    @Operation(
            summary = "Buscar estoque por código",
            description = "Retorna os detalhes de um estoque específico através do seu código identificador"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Estoque encontrado com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = EstoqueResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Estoque não encontrado",
                    content = @Content
            )
    })
    public ResponseEntity<EstoqueResponseDTO> consultar(
            @Parameter(description = "Código do estoque", required = true, example = "1")
            @PathVariable Long cdEstoque) {

        EstoqueResponseDTO estoque = estoqueService.consultarEstoque(cdEstoque);
        return ResponseEntity.status(HttpStatus.OK).body(estoque);
    }

    @GetMapping("/buscar/todos")
    @Operation(
            summary = "Listar todos os estoques",
            description = "Retorna uma lista com todos os registros de estoque cadastrados no sistema"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista de estoques retornada com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = EstoqueResponseDTO.class)
                    )
            )
    })
    public ResponseEntity<List<EstoqueResponseDTO>> consultarTodos() {
        var estoque = estoqueService.consultarTodos();
        return ResponseEntity.status(HttpStatus.OK).body(estoque);
    }

    @GetMapping("/produto/{cdProduto}")
    @Operation(
            summary = "Buscar estoque por produto",
            description = "Retorna o estoque total de um produto específico (soma de todos os estoques ativos do produto)"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Estoque encontrado com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = EstoqueResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Produto não encontrado ou sem estoque",
                    content = @Content
            )
    })
    public ResponseEntity<EstoqueResponseDTO> buscarPorProduto(
            @Parameter(description = "Código do produto", required = true, example = "1")
            @PathVariable Long cdProduto) {
        
        List<EstoqueResponseDTO> estoques = estoqueService.consultarEstoquesPorProduto(cdProduto);
        
        if (estoques.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        
        Integer qtTotal = estoques.stream()
                .filter(e -> e.flAtivo())
                .mapToInt(e -> e.qtEstoque() != null ? e.qtEstoque() : 0)
                .sum();
        
        EstoqueResponseDTO primeiro = estoques.get(0);
        EstoqueResponseDTO estoqueTotal = new EstoqueResponseDTO(
                primeiro.cdEstoque(), qtTotal, true, primeiro.cdProduto(), primeiro.nmProduto());
        
        return ResponseEntity.ok(estoqueTotal);
    }

    @PutMapping("/baixar-estoque-ficticio/{cdProduto}")
    @Operation(
            summary = "Baixar estoque para pedido fictício",
            description = "Baixa estoque de um produto para finalização de pedido fictício (não requer autenticação)"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Estoque baixado com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = EstoqueResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Dados inválidos ou estoque insuficiente",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Produto não encontrado",
                    content = @Content
            )
    })
    public ResponseEntity<EstoqueResponseDTO> baixarEstoqueFicticio(
            @Parameter(description = "Código do produto", required = true, example = "1")
            @PathVariable Long cdProduto,
            @Parameter(description = "Quantidade a baixar", required = true)
            @RequestParam Integer quantidade) {
        
        if (!estoqueService.temEstoque(cdProduto, quantidade)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        
        estoqueService.baixarEstoque(cdProduto, quantidade);
        List<EstoqueResponseDTO> estoques = estoqueService.consultarEstoquesPorProduto(cdProduto);
        
        if (estoques.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        
        Integer qtTotal = estoques.stream()
                .filter(e -> e.flAtivo())
                .mapToInt(e -> e.qtEstoque() != null ? e.qtEstoque() : 0)
                .sum();
        
        EstoqueResponseDTO primeiro = estoques.get(0);
        EstoqueResponseDTO estoqueAtualizado = new EstoqueResponseDTO(
                primeiro.cdEstoque(), qtTotal, true, primeiro.cdProduto(), primeiro.nmProduto());
        
        return ResponseEntity.ok(estoqueAtualizado);
    }

    @PutMapping("/atualizar/{cdEstoque}")
    @Operation(
            summary = "Atualizar estoque",
            description = "Atualiza os dados de um estoque existente através do seu código identificador"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Estoque atualizado com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = EstoqueResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Dados inválidos fornecidos",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Estoque não encontrado",
                    content = @Content
            )
    })
    public ResponseEntity<EstoqueResponseDTO> atualizarEstoque(
            @Parameter(description = "Código do estoque", required = true, example = "1")
            @PathVariable Long cdEstoque,
            @Parameter(description = "Dados atualizados do estoque", required = true)
            @RequestBody @Valid EstoqueRequestDTO estoqueRequest) {

        EstoqueResponseDTO estoque = estoqueService.atualizarEstoque(cdEstoque, estoqueRequest);
        return ResponseEntity.status(HttpStatus.OK).body(estoque);
    }

    @DeleteMapping("/desativar/{cdEstoque}")
    @Operation(
            summary = "Desativar estoque",
            description = "Desativa um estoque existente através do seu código identificador (exclusão lógica)"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Estoque desativado com sucesso",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Estoque não encontrado",
                    content = @Content
            )
    })
    public ResponseEntity<EstoqueResponseDTO> desativarEstoque(
            @Parameter(description = "Código do estoque", required = true, example = "1")
            @PathVariable Long cdEstoque) {

        var estoque = estoqueService.desativarEstoque(cdEstoque);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(estoque);
    }

    @PutMapping("/reativar/{cdEstoque}")
    @Operation(
            summary = "Reativar estoque",
            description = "Reativa um estoque previamente desativado através do seu código identificador"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Estoque reativado com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = EstoqueResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Estoque não encontrado",
                    content = @Content
            )
    })
    public ResponseEntity<EstoqueResponseDTO> reativarEstoque(
            @Parameter(description = "Código do estoque", required = true, example = "1")
            @PathVariable Long cdEstoque) {

        var estoque = estoqueService.reativarEstoque(cdEstoque);
        return ResponseEntity.status(HttpStatus.OK).body(estoque);
    }
}