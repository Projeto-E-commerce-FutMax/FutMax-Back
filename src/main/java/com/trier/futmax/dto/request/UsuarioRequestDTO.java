package com.trier.futmax.dto.request;


import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.br.CPF;

public record UsuarioRequestDTO(

        Long cdUsuario,

        @NotBlank(message = "O nome do usuário é obrigatório")
        @Size(min = 3, max = 50, message = "O nome deve ter entre 3 e 50 caracteres")
        String nmUsuario,

        @NotBlank(message = "O e-mail é obrigatório")
        @Email(message = "E-mail inválido")
        @Size(max = 100, message = "O e-mail deve ter no máximo 100 caracteres")
        String nmEmail,

        @NotBlank(message = "O CPF é obrigatório")
        @CPF(message = "CPF inválido")
        String nmCpf,

        @NotBlank(message = "A senha é obrigatória")
        @Size(min = 6, max = 20, message = "A senha deve ter entre 6 e 20 caracteres")
        @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).*$",
                message = "A senha deve conter pelo menos uma letra maiúscula, uma minúscula, um número e um caractere especial")
        String nmSenha,

        @NotNull(message = "O telefone é obrigatório")
        String nmTelefone,

        @NotBlank(message = "O endereço é obrigatório")
        @Size(max = 100, message = "O endereço deve ter no máximo 100 caracteres")
        String nmEndereco,

        @Size(max = 120, message = "A descrição do endereço deve ter no máximo 120 caracteres")
        String dsEndereco,

        @NotNull(message = "O status ativo é obrigatório")
        Boolean flAtivo

) { }

