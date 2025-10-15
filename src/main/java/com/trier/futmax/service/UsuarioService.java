package com.trier.futmax.service;

import com.trier.futmax.dto.request.UsuarioRequestDTO;
import com.trier.futmax.dto.response.UsuarioResponseDTO;
import com.trier.futmax.model.RoleModel;
import com.trier.futmax.model.UsuarioModel;
import com.trier.futmax.repository.RoleRepository;
import com.trier.futmax.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final RoleRepository roleRepository;

    @Transactional
    public UsuarioResponseDTO salvar(UsuarioRequestDTO dto) {
        UsuarioModel usuario = new UsuarioModel();
        usuario.setNmUsuario(dto.nmUsuario());
        usuario.setNmEmail(dto.nmEmail());
        usuario.setNmCpf(dto.nmCpf());
        usuario.setNmTelefone(dto.nmTelefone());
        usuario.setNmEndereco(dto.nmEndereco());
        usuario.setDsEndereco(dto.dsEndereco());
        usuario.setFlAtivo(dto.flAtivo());


        UsuarioModel usuarioSalvo = usuarioRepository.save(usuario);
        return converterParaResponse(usuarioSalvo);
    }


    public List<UsuarioResponseDTO> listar() {
        return usuarioRepository.findAll().stream()
                .map(this::converterParaResponse)
                .collect(Collectors.toList());
    }

    private UsuarioResponseDTO converterParaResponse(UsuarioModel usuario) {
        return new UsuarioResponseDTO(
                usuario.getCdUsuario(),
                usuario.getNmUsuario(),
                usuario.getNmEmail(),
                usuario.getNmCpf(),
                usuario.getNmTelefone(),
                usuario.getNmEndereco(),
                usuario.getDsEndereco(),
                usuario.getFlAtivo()
        );
    }

}