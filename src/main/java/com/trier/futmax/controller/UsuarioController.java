package com.trier.futmax.controller;

import com.trier.futmax.dto.request.UsuarioRequestDTO;
import com.trier.futmax.dto.response.UsuarioResponseDTO;
import com.trier.futmax.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/usuarios")
@Tag(name = "Usuário", description = "API para gerenciamento de usuários")
public class UsuarioController {

    private final UsuarioService service;

    @PostMapping("/cadastrar")
    @Operation(
            summary = "Criar um novo usuário",
            description = "Cria um novo usuário com os dados fornecidos e retorna o usuário criado com seu identificador"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Usuário criado com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UsuarioResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Dados inválidos fornecidos",
                    content = @Content
            ),
    })
    public ResponseEntity<UsuarioResponseDTO> criar(
            @Parameter(description = "Dados do usuário a ser criado", required = true)
            @Valid @RequestBody UsuarioRequestDTO dto) {
        UsuarioResponseDTO usuarioCriado = service.salvar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioCriado);
    }

    @GetMapping("/buscar/todos")
    @Operation(
            summary = "Listar todos os usuários",
            description = "Retorna uma lista com todos os usuários cadastrados no sistema"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista de usuários retornada com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UsuarioResponseDTO.class)
                    )
            ),
    })
    public ResponseEntity<List<UsuarioResponseDTO>> listar() {
        return ResponseEntity.status(HttpStatus.OK).body(service.listar());
    }

    @DeleteMapping("/desativar/{cdUsuario}")
    @Operation(
            summary = "Desativar um usuário",
            description = "Desativa um usuário existente através do seu código identificador sem removê-lo do sistema"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Usuário desativado com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UsuarioResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Usuário não encontrado",
                    content = @Content
            ),
    })
    public ResponseEntity<UsuarioResponseDTO> desativar(
            @Parameter(description = "Código do usuário", required = true, example = "1")
            @PathVariable Long cdUsuario) {
        var desativar = service.desativar(cdUsuario);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(desativar);
    }

    @PutMapping("/reativar/{cdUsuario}")
    @Operation(
            summary = "Reativar um usuário",
            description = "Reativa um usuário previamente desativado através do seu código identificador"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Usuário reativado com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UsuarioResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Usuário não encontrado",
                    content = @Content
            ),
    })
    public ResponseEntity<UsuarioResponseDTO> reativar(
            @Parameter(description = "Código do usuário", required = true, example = "1")
            @PathVariable @Valid Long cdUsuario) {
        var reativar = service.reativar(cdUsuario);
        return ResponseEntity.status(HttpStatus.OK).body(reativar);
    }
}