package com.trier.futmax.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record RoleRequestDTO(

        @NotBlank(message = "O nome da role é obrigatório")
        @Pattern(regexp = "^ROLE_[A-Z_]+$",
                message = "O nome da role deve seguir o padrão ROLE_NOME (ex: ROLE_ADMIN, ROLE_USER)")
        String nmRole

) {
}