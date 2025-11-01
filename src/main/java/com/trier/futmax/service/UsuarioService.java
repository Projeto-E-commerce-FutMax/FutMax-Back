package com.trier.futmax.service;

import com.trier.futmax.dto.request.UsuarioRequestDTO;
import com.trier.futmax.dto.response.UsuarioResponseDTO;
import com.trier.futmax.model.RoleModel;
import com.trier.futmax.model.UsuarioModel;
import com.trier.futmax.repository.RoleRepository;
import com.trier.futmax.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UsuarioResponseDTO salvar(UsuarioRequestDTO dto) {
        if (usuarioRepository.findByNmEmail(dto.nmEmail()).isPresent()) {
            throw new RuntimeException("E-mail já cadastrado!");
        }

        if (usuarioRepository.findByNmCpf(dto.nmCpf()).isPresent()) {
            throw new RuntimeException("CPF já cadastrado!");
        }

        UsuarioModel usuario = new UsuarioModel();
        usuario.setNmUsuario(dto.nmUsuario());
        usuario.setNmEmail(dto.nmEmail());
        usuario.setNmCpf(dto.nmCpf());
        usuario.setNmSenha(passwordEncoder.encode(dto.nmSenha()));
        usuario.setNmTelefone(dto.nmTelefone());
        usuario.setNmEndereco(dto.nmEndereco());
        usuario.setDsEndereco(dto.dsEndereco());
        usuario.setFlAtivo(dto.flAtivo());

        Set<RoleModel> roles = new HashSet<>();

        if (dto.roles() != null && !dto.roles().isEmpty()) {
            // Se roles foram enviadas no DTO, usa elas
            for (String roleName : dto.roles()) {
                RoleModel role = roleRepository.findByNmRole(roleName)
                        .orElseGet(() -> {
                            RoleModel newRole = new RoleModel();
                            newRole.setNmRole(roleName);
                            return roleRepository.save(newRole);
                        });
                roles.add(role);
            }
        } else {
            RoleModel roleUser = roleRepository.findByNmRole("ROLE_USER")
                    .orElseGet(() -> {
                        RoleModel newRole = new RoleModel();
                        newRole.setNmRole("ROLE_USER");
                        return roleRepository.save(newRole);
                    });
            roles.add(roleUser);
        }

        usuario.setRoleModels(roles);

        UsuarioModel usuarioSalvo = usuarioRepository.save(usuario);
        return converterParaResponse(usuarioSalvo);
    }

    public List<UsuarioResponseDTO> listar() {
        List<UsuarioModel> usuario = usuarioRepository.findAllByFlAtivo();
        return usuario.stream().map(this::converterParaResponse).collect(Collectors.toList());
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

    @Transactional
    public UsuarioResponseDTO desativar(Long cdUsuario) {
        UsuarioModel usuario = usuarioRepository.findById(cdUsuario)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado!"));

        usuario.setFlAtivo(false);
        usuarioRepository.save(usuario);

        return converterParaResponse(usuario);
    }

    @Transactional
    public UsuarioResponseDTO reativar(Long cdUsuario) {
        UsuarioModel usuario = usuarioRepository.findById(cdUsuario)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado!"));

        usuario.setFlAtivo(true);
        usuarioRepository.save(usuario);

        return converterParaResponse(usuario);
    }
}