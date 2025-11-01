package com.trier.futmax.dto.response;

import com.trier.futmax.model.UsuarioModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class AuthResponseDTO {

    private String token;
    private UsuarioModel usuario;

}