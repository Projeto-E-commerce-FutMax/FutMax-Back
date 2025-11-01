package com.trier.futmax.repository;

import com.trier.futmax.model.RoleModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<RoleModel, Long> {
    Optional<RoleModel> findByNmRole(String nmRole);
}