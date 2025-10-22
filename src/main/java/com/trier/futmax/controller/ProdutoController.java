package com.trier.futmax.controller;

import com.trier.futmax.dto.request.ProdutoRequestDTO;
import com.trier.futmax.dto.response.ProdutoResponseDTO;
import com.trier.futmax.service.ProdutoService;
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
@RequestMapping("/api/produto")
@Tag(name = "Produto", description = "API para gerenciamento de produtos")
public class ProdutoController {

    @Autowired
    private ProdutoService produtoService;

    @PostMapping("/cadastrar")
    @Operation(
            summary = "Cadastrar um novo produto",
            description = "Cria um novo produto com os dados fornecidos e retorna o produto criado com seu identificador"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Produto cadastrado com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProdutoResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Dados inválidos fornecidos",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Erro interno do servidor",
                    content = @Content
            )
    })
    public ResponseEntity<ProdutoResponseDTO> cadastrarProduto(
            @Parameter(description = "Dados do produto a ser cadastrado", required = true)
            @RequestBody @Valid ProdutoRequestDTO produtoRequest) {

        var produto = produtoService.cadastrarProduto(produtoRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(produto);
    }

    @GetMapping("/buscar/{cdProduto}")
    @Operation(
            summary = "Buscar produto por código",
            description = "Retorna os detalhes de um produto específico através do seu código identificador"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Produto encontrado com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProdutoResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Produto não encontrado",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Erro interno do servidor",
                    content = @Content
            )
    })
    public ResponseEntity<ProdutoResponseDTO> consultar(
            @Parameter(description = "Código do produto", required = true, example = "1")
            @PathVariable Long cdProduto) {

        ProdutoResponseDTO produto = produtoService.consultarProduto(cdProduto);
        return ResponseEntity.status(HttpStatus.OK).body(produto);
    }

    @GetMapping("/buscar/todos")
    @Operation(
            summary = "Listar todos os produtos",
            description = "Retorna uma lista com todos os produtos cadastrados no sistema"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista de produtos retornada com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProdutoResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Erro interno do servidor",
                    content = @Content
            )
    })
    public ResponseEntity<List<ProdutoResponseDTO>> buscarTodos() {
        var produto = produtoService.buscarTodos();
        return ResponseEntity.status(HttpStatus.OK).body(produto);
    }

    @PutMapping("/atualizar/{cdProduto}")
    @Operation(
            summary = "Atualizar produto",
            description = "Atualiza os dados de um produto existente através do seu código identificador"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Produto atualizado com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProdutoResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Dados inválidos fornecidos",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Produto não encontrado",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Erro interno do servidor",
                    content = @Content
            )
    })
    public ResponseEntity<ProdutoResponseDTO> atualizarProduto(
            @Parameter(description = "Código do produto", required = true, example = "1")
            @PathVariable Long cdProduto,
            @Parameter(description = "Dados atualizados do produto", required = true)
            @RequestBody @Valid ProdutoRequestDTO produtoRequestDTO) {

        ProdutoResponseDTO produto = produtoService.atualizarProduto(cdProduto, produtoRequestDTO);
        return ResponseEntity.ok(produto);
    }

    @DeleteMapping("/desativar/{cdProduto}")
    @Operation(
            summary = "Desativar produto",
            description = "Desativa um produto existente através do seu código identificador (exclusão lógica)"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Produto desativado com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProdutoResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Produto não encontrado",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Erro interno do servidor",
                    content = @Content
            )
    })
    public ResponseEntity<ProdutoResponseDTO> desativarProduto(
            @Parameter(description = "Código do produto", required = true, example = "1")
            @PathVariable Long cdProduto) {

        var produto = produtoService.desativarProduto(cdProduto);
        return ResponseEntity.status(HttpStatus.OK).body(produto);
    }

    @PutMapping("/reativar/{cdProduto}")
    @Operation(
            summary = "Reativar produto",
            description = "Reativa um produto previamente desativado através do seu código identificador"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Produto reativado com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProdutoResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Produto não encontrado",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Erro interno do servidor",
                    content = @Content
            )
    })
    public ResponseEntity<ProdutoResponseDTO> reativarProduto(
            @Parameter(description = "Código do produto", required = true, example = "1")
            @PathVariable Long cdProduto) {

        var produto = produtoService.reativarProduto(cdProduto);
        return ResponseEntity.status(HttpStatus.OK).body(produto);
    }
}