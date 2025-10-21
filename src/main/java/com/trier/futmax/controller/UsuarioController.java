package com.trier.futmax.controller;

import com.trier.futmax.dto.request.UsuarioRequestDTO;
import com.trier.futmax.dto.response.UsuarioResponseDTO;
import com.trier.futmax.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService service;

    @PostMapping("/cadastrar")
    public ResponseEntity<UsuarioResponseDTO> criar(@Valid @RequestBody UsuarioRequestDTO dto) {
        UsuarioResponseDTO usuarioCriado = service.salvar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioCriado);
    }

    @GetMapping("/buscar/todos")
    public ResponseEntity<List<UsuarioResponseDTO>> listar() {
        return ResponseEntity.status(HttpStatus.OK).body(service.listar());
    }

    @DeleteMapping("/desativar/{cdUsuario}")
    public ResponseEntity<UsuarioResponseDTO> desativar(@PathVariable Long cdUsuario) {
        var desativar = service.desativar(cdUsuario);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(desativar);
    }

    @PutMapping("/reativar/{cdUsuario}")
    public ResponseEntity<UsuarioResponseDTO> reativar(@PathVariable @Valid Long cdUsuario) {
        var reativar = service.reativar(cdUsuario);
        return ResponseEntity.status(HttpStatus.OK).body(reativar);
    }



}