package com.trier.futmax.controller;

import com.trier.futmax.dto.request.AuthRequestDTO;
import com.trier.futmax.dto.response.AuthResponseDTO;
import com.trier.futmax.model.UsuarioModel;
import com.trier.futmax.repository.UsuarioRepository;
import com.trier.futmax.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Autenticação", description = "API para autenticação de usuários")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;
    private final UsuarioRepository usuarioRepository; // ADICIONE ESTA LINHA

    @PostMapping("/login")
    @Operation(summary = "Realizar login", description = "Autentica o usuário e retorna um token JWT")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody AuthRequestDTO authRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authRequest.getNmEmail(),
                            authRequest.getNmSenha()
                    )
            );

            UserDetails userDetails = userDetailsService.loadUserByUsername(authRequest.getNmEmail());
            String token = jwtUtil.generateToken(userDetails);

            UsuarioModel usuario = usuarioRepository.findByNmEmail(authRequest.getNmEmail())
                    .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

            return ResponseEntity.ok(new AuthResponseDTO(token, usuario));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}