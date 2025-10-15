package com.trier.futmax.dto.response;


public record UsuarioResponseDTO(

        Long cdUsuario,

        String nmUsuario,

        String nmEmail,

        String nmCpf,

        Integer nmTelefone,

        String nmEndereco,

        String dsEndereco,

        Boolean flAtivo



) {


