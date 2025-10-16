package com.trier.futmax.controller;

import com.trier.futmax.dto.request.RoleRequestDTO;
import com.trier.futmax.dto.response.RoleResponseDTO;
import com.trier.futmax.model.RoleModel;
import com.trier.futmax.service.RoleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/role")
public class RoleController {

    public final RoleService roleService;

    @PostMapping("/cadastrar")
    public ResponseEntity<RoleResponseDTO> cadastrarRole(@RequestBody @Valid RoleRequestDTO roleRequest) {
        var role = roleService.cadastrarRole(roleRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(role);
    }

    @GetMapping("/buscar/{cdRole}")
    public ResponseEntity<RoleResponseDTO> consultar(@PathVariable Long cdRole) {
        RoleResponseDTO role = roleService.consultarRole(cdRole);
        return ResponseEntity.status(HttpStatus.OK).body(role);
    }

    @GetMapping("/buscar/todos")
    public ResponseEntity<List<RoleModel>> consultarTodos() {
        var roles = roleService.consultarTodos();
        return ResponseEntity.status(HttpStatus.OK).body(roles);
    }

    @PutMapping("/atualizar/{cdRole}")
    public ResponseEntity<RoleResponseDTO> atualizarRole(@PathVariable Long cdRole,
                                                         @RequestBody @Valid RoleRequestDTO roleRequest) {
        RoleResponseDTO role = roleService.atualizarRole(cdRole, roleRequest);
        return ResponseEntity.status(HttpStatus.OK).body(role);
    }

    @DeleteMapping("/deletar/{cdRole}")
    public ResponseEntity<Void> deletarRole(@PathVariable Long cdRole) {
        roleService.removerRole(cdRole);
        return ResponseEntity.noContent().build();
    }
}