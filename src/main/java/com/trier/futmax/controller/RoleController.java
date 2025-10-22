package com.trier.futmax.controller;

import com.trier.futmax.dto.request.RoleRequestDTO;
import com.trier.futmax.dto.response.RoleResponseDTO;
import com.trier.futmax.model.RoleModel;
import com.trier.futmax.service.RoleService;
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
@RequestMapping("/api/role")
@Tag(name = "Role", description = "API para gerenciamento de perfis de usuário")
public class RoleController {

    public final RoleService roleService;

    @PostMapping("/cadastrar")
    @Operation(
            summary = "Cadastrar uma nova role",
            description = "Cria um novo perfil de usuário com os dados fornecidos e retorna o perfil criado com seu identificador"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Role cadastrada com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = RoleResponseDTO.class)
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
    public ResponseEntity<RoleResponseDTO> cadastrarRole(
            @Parameter(description = "Dados da role a ser cadastrada", required = true)
            @RequestBody @Valid RoleRequestDTO roleRequest) {
        var role = roleService.cadastrarRole(roleRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(role);
    }

    @GetMapping("/buscar/{cdRole}")
    @Operation(
            summary = "Buscar role por código",
            description = "Retorna os detalhes de uma role específica através do seu código identificador"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Role encontrada com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = RoleResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Role não encontrada",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Erro interno do servidor",
                    content = @Content
            )
    })
    public ResponseEntity<RoleResponseDTO> consultar(
            @Parameter(description = "Código da role", required = true, example = "1")
            @PathVariable Long cdRole) {
        RoleResponseDTO role = roleService.consultarRole(cdRole);
        return ResponseEntity.status(HttpStatus.OK).body(role);
    }

    @GetMapping("/buscar/todos")
    @Operation(
            summary = "Listar todas as roles",
            description = "Retorna uma lista com todas as roles cadastradas no sistema"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista de roles retornada com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = RoleModel.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Erro interno do servidor",
                    content = @Content
            )
    })
    public ResponseEntity<List<RoleModel>> consultarTodos() {
        var roles = roleService.consultarTodos();
        return ResponseEntity.status(HttpStatus.OK).body(roles);
    }

    @PutMapping("/atualizar/{cdRole}")
    @Operation(
            summary = "Atualizar uma role",
            description = "Atualiza os dados de uma role existente através do seu código identificador"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Role atualizada com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = RoleResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Dados inválidos fornecidos",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Role não encontrada",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Erro interno do servidor",
                    content = @Content
            )
    })
    public ResponseEntity<RoleResponseDTO> atualizarRole(
            @Parameter(description = "Código da role", required = true, example = "1")
            @PathVariable Long cdRole,
            @Parameter(description = "Dados atualizados da role", required = true)
            @RequestBody @Valid RoleRequestDTO roleRequest) {
        RoleResponseDTO role = roleService.atualizarRole(cdRole, roleRequest);
        return ResponseEntity.status(HttpStatus.OK).body(role);
    }

    @DeleteMapping("/deletar/{cdRole}")
    @Operation(
            summary = "Deletar uma role",
            description = "Remove uma role do sistema através do seu código identificador"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Role deletada com sucesso",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Role não encontrada",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Erro interno do servidor",
                    content = @Content
            )
    })
    public ResponseEntity<Void> deletarRole(
            @Parameter(description = "Código da role", required = true, example = "1")
            @PathVariable Long cdRole) {
        roleService.removerRole(cdRole);
        return ResponseEntity.noContent().build();
    }
}