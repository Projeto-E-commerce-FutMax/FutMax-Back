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
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;

@RestController
@RequestMapping("/api/produto")
@Tag(name = "Produto", description = "API para gerenciamento de produtos")
public class ProdutoController {
    private static final Path UPLOAD_DIR = Paths.get(System.getProperty("user.dir"), "uploads").toAbsolutePath().normalize();


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

    @PostMapping(value = "/cadastrar-com-imagem", consumes = {org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE})
    @Operation(
            summary = "Cadastrar produto com imagem",
            description = "Cria um novo produto recebendo os campos e um arquivo de imagem"
    )
    public ResponseEntity<ProdutoResponseDTO> cadastrarProdutoComImagem(
            @RequestParam("nmProduto") String nmProduto,
            @RequestParam("vlProduto") Double vlProduto,
            @RequestParam("dsProduto") String dsProduto,
            @RequestParam("flAtivo") Boolean flAtivo,
            @RequestPart(value = "imagem", required = false) MultipartFile imagem
    ) {
        String imgUrl = null;
        if (imagem != null && !imagem.isEmpty()) {
            try {
                // Validar tamanho (5MB max)
                if (imagem.getSize() > 5 * 1024 * 1024) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body(null);
                }

                // Validar tipo
                String contentType = imagem.getContentType();
                if (contentType == null || !contentType.startsWith("image/")) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body(null);
                }

                Files.createDirectories(UPLOAD_DIR);
                String filename = System.currentTimeMillis() + "_" + imagem.getOriginalFilename();
                Path filePath = UPLOAD_DIR.resolve(filename);
                imagem.transferTo(filePath);
                imgUrl = "/api/produto/imagens/" + filename;
            } catch (Exception e) {
                e.printStackTrace();
                throw new org.springframework.web.server.ResponseStatusException(
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        "Falha ao salvar arquivo de imagem"
                );
            }
        }

        ProdutoRequestDTO dto = new ProdutoRequestDTO(null, nmProduto, vlProduto, dsProduto, flAtivo, imgUrl);
        var produto = produtoService.cadastrarProduto(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(produto);
    }

    @GetMapping("/imagens/{filename}")
    @Operation(summary = "Servir imagem de produto")
    public ResponseEntity<Resource> obterImagem(@PathVariable String filename) {
        try {
            Path filePath = UPLOAD_DIR.resolve(filename).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (!resource.exists()) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(resource);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
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

    @PostMapping(value = "/atualizar-com-imagem/{cdProduto}", consumes = {org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE})
    @Operation(summary = "Atualizar produto com imagem")
    public ResponseEntity<ProdutoResponseDTO> atualizarProdutoComImagem(
            @PathVariable Long cdProduto,
            @RequestParam("nmProduto") String nmProduto,
            @RequestParam("vlProduto") Double vlProduto,
            @RequestParam("dsProduto") String dsProduto,
            @RequestParam("flAtivo") Boolean flAtivo,
            @RequestPart(value = "imagem", required = false) MultipartFile imagem
    ) {
        String imgUrl = null;
        if (imagem != null && !imagem.isEmpty()) {
            try {
                // Validar tamanho (5MB max)
                if (imagem.getSize() > 5 * 1024 * 1024) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body(null);
                }

                // Validar tipo
                String contentType = imagem.getContentType();
                if (contentType == null || !contentType.startsWith("image/")) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body(null);
                }

                Files.createDirectories(UPLOAD_DIR);
                String filename = System.currentTimeMillis() + "_" + imagem.getOriginalFilename();
                Path filePath = UPLOAD_DIR.resolve(filename);
                imagem.transferTo(filePath);
                imgUrl = "/api/produto/imagens/" + filename;
            } catch (Exception e) {
                e.printStackTrace();
                throw new org.springframework.web.server.ResponseStatusException(
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        "Falha ao salvar arquivo de imagem"
                );
            }
        }
        ProdutoRequestDTO dto = new ProdutoRequestDTO(cdProduto, nmProduto, vlProduto, dsProduto, flAtivo, imgUrl);
        ProdutoResponseDTO produto = produtoService.atualizarProduto(cdProduto, dto);
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