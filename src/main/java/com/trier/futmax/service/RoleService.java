package com.trier.futmax.service;

import com.trier.futmax.dto.request.RoleRequestDTO;
import com.trier.futmax.dto.response.RoleResponseDTO;
import com.trier.futmax.model.RoleModel;
import com.trier.futmax.repository.RoleRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleService {

    public final RoleRepository roleRepository;

    @Transactional
    public RoleResponseDTO cadastrarRole(RoleRequestDTO roleRequest) {

        var role = new RoleModel();
        role.setNmRole(roleRequest.nmRole());

        roleRepository.save(role);

        return new RoleResponseDTO(
                role.getCdRole(),
                role.getNmRole()
        );
    }

    @Transactional
    public RoleResponseDTO consultarRole(Long cdRole) {

        RoleModel role = roleRepository.findById(cdRole)
                .orElseThrow(() -> new EntityNotFoundException("Role não encontrada para o ID: " + cdRole));

        return new RoleResponseDTO(
                role.getCdRole(),
                role.getNmRole()
        );
    }

    public List<RoleModel> consultarTodos() {
        return roleRepository.findAll();
    }

    @Transactional
    public RoleResponseDTO atualizarRole(Long cdRole, RoleRequestDTO roleRequest) {

        RoleModel role = roleRepository.findById(cdRole)
                .orElseThrow(() -> new EntityNotFoundException("Role não encontrada para o ID: " + cdRole));

        role.setNmRole(roleRequest.nmRole());

        roleRepository.save(role);

        return new RoleResponseDTO(
                role.getCdRole(),
                role.getNmRole()
        );
    }

    @Transactional
    public void removerRole(Long cdRole) {

        RoleModel role = roleRepository.findById(cdRole)
                .orElseThrow(() -> new EntityNotFoundException("Role não encontrada para o ID: " + cdRole));

        roleRepository.delete(role);
    }
}