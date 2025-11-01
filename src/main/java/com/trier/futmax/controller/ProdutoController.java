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
    
    // Caminhos possíveis para pasta img do frontend (fallback)
    private static Path getFrontendImgDir() {
        // Tentar diferentes caminhos possíveis
        Path[] possiblePaths = {
            Paths.get(System.getProperty("user.dir"), "..", "FutMax-Front-main", "img").toAbsolutePath().normalize(),
            Paths.get(System.getProperty("user.dir"), "..", "..", "FutMax-Front-main", "img").toAbsolutePath().normalize(),
            Paths.get(System.getProperty("user.dir"), "FutMax-Front-main", "img").toAbsolutePath().normalize(),
            Paths.get(System.getProperty("user.dir"), "img").toAbsolutePath().normalize()
        };
        
        for (Path path : possiblePaths) {
            if (Files.exists(path) && Files.isDirectory(path)) {
                System.out.println("Pasta img do frontend encontrada em: " + path.toString());
                return path;
            }
        }
        
        // Retornar o primeiro caminho por padrão (será verificado depois)
        return possiblePaths[0];
    }


    @Autowired
    private ProdutoService produtoService;

    @PostMapping(value = "/cadastrar", consumes = {org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE})
    @Operation(
            summary = "Cadastrar um novo produto",
            description = "Cria um novo produto com os dados fornecidos e opcionalmente uma imagem"
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
            )
    })
    public ResponseEntity<ProdutoResponseDTO> cadastrarProduto(
            @RequestParam("nmProduto") String nmProduto,
            @RequestParam("vlProduto") Double vlProduto,
            @RequestParam("dsProduto") String dsProduto,
            @RequestParam("flAtivo") Boolean flAtivo,
            @RequestParam("nmCategoria") String nmCategoria,
            @RequestPart(value = "imagem", required = false) MultipartFile imagem
    ) {
        String imgUrl = null;
        if (imagem != null && !imagem.isEmpty()) {
            try {
                if (imagem.getSize() > 5 * 1024 * 1024 || 
                    imagem.getContentType() == null || 
                    !imagem.getContentType().startsWith("image/")) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
                }
                
                Files.createDirectories(UPLOAD_DIR);
                String filename = System.currentTimeMillis() + "_" + imagem.getOriginalFilename();
                imagem.transferTo(UPLOAD_DIR.resolve(filename));
                imgUrl = "/api/produto/imagens/" + filename;
            } catch (Exception e) {
                throw new org.springframework.web.server.ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "Falha ao salvar imagem");
            }
        }

        ProdutoRequestDTO dto = new ProdutoRequestDTO(null, nmProduto, vlProduto, dsProduto, flAtivo, imgUrl, nmCategoria);
        var produto = produtoService.cadastrarProduto(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(produto);
    }

    @GetMapping("/imagens/{filename}")
    @Operation(summary = "Servir imagem de produto")
    public ResponseEntity<Resource> obterImagem(@PathVariable String filename) {
        try {
            Path filePath = UPLOAD_DIR.resolve(filename).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            
            if (!resource.exists() || !resource.isReadable()) {
                // Tentar fallback: remover timestamp do nome se existir
                String originalFilename = filename;
                if (filename.contains("_")) {
                    int underscoreIndex = filename.indexOf("_");
                    if (underscoreIndex > 0 && underscoreIndex < filename.length() - 1) {
                        try {
                            Long.parseLong(filename.substring(0, underscoreIndex));
                            originalFilename = filename.substring(underscoreIndex + 1);
                        } catch (NumberFormatException e) {
                            // Não é timestamp
                        }
                    }
                }
                
                // Tentar servir da pasta img do frontend
                Path fallbackPath = getFrontendImgDir().resolve(originalFilename).normalize();
                try {
                    Resource fallbackResource = new UrlResource(fallbackPath.toUri());
                    if (fallbackResource.exists() && fallbackResource.isReadable()) {
                        String contentType = getContentType(originalFilename);
                        return ResponseEntity.ok()
                                .header(org.springframework.http.HttpHeaders.CONTENT_TYPE, contentType)
                                .body(fallbackResource);
                    }
                } catch (Exception e) {
                    // Fallback falhou
                }
                return ResponseEntity.notFound().build();
            }
            
            String contentType = getContentType(filename);
            return ResponseEntity.ok()
                    .header(org.springframework.http.HttpHeaders.CONTENT_TYPE, contentType)
                    .body(resource);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    private String getContentType(String filename) {
        String lower = filename.toLowerCase();
        if (lower.endsWith(".jpg") || lower.endsWith(".jpeg")) return "image/jpeg";
        if (lower.endsWith(".png")) return "image/png";
        if (lower.endsWith(".gif")) return "image/gif";
        if (lower.endsWith(".webp")) return "image/webp";
        if (lower.endsWith(".avif")) return "image/avif";
        return "application/octet-stream";
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
            )
    })
    public ResponseEntity<List<ProdutoResponseDTO>> buscarTodos() {
        var produto = produtoService.buscarTodos();
        return ResponseEntity.status(HttpStatus.OK).body(produto);
    }

    @PutMapping(value = "/atualizar/{cdProduto}", consumes = {org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE})
    @Operation(
            summary = "Atualizar produto",
            description = "Atualiza os dados de um produto existente e opcionalmente uma imagem"
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
            )
    })
    public ResponseEntity<ProdutoResponseDTO> atualizarProduto(
            @Parameter(description = "Código do produto", required = true, example = "1")
            @PathVariable Long cdProduto,
            @RequestParam("nmProduto") String nmProduto,
            @RequestParam("vlProduto") Double vlProduto,
            @RequestParam("dsProduto") String dsProduto,
            @RequestParam("flAtivo") Boolean flAtivo,
            @RequestParam("nmCategoria") String nmCategoria,
            @RequestPart(value = "imagem", required = false) MultipartFile imagem
    ) {
        String imgUrl = null;
        if (imagem != null && !imagem.isEmpty()) {
            try {
                if (imagem.getSize() > 5 * 1024 * 1024) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
                }

                String contentType = imagem.getContentType();
                if (contentType == null || !contentType.startsWith("image/")) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
                }

                Files.createDirectories(UPLOAD_DIR);
                String filename = System.currentTimeMillis() + "_" + imagem.getOriginalFilename();
                imagem.transferTo(UPLOAD_DIR.resolve(filename));
                imgUrl = "/api/produto/imagens/" + filename;
            } catch (Exception e) {
                throw new org.springframework.web.server.ResponseStatusException(
                        HttpStatus.INTERNAL_SERVER_ERROR, "Falha ao salvar imagem");
            }
        }
        
        ProdutoRequestDTO dto = new ProdutoRequestDTO(cdProduto, nmProduto, vlProduto, dsProduto, flAtivo, imgUrl, nmCategoria);
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
            )
    })
    public ResponseEntity<ProdutoResponseDTO> reativarProduto(
            @Parameter(description = "Código do produto", required = true, example = "1")
            @PathVariable Long cdProduto) {

        var produto = produtoService.reativarProduto(cdProduto);
        return ResponseEntity.status(HttpStatus.OK).body(produto);
    }
}