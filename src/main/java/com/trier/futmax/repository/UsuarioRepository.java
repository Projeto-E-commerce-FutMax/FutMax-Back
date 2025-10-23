package com.trier.futmax.repository;

import com.trier.futmax.model.UsuarioModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<UsuarioModel, Long> {

    Optional<UsuarioModel> findByNmEmail(String nmEmail);

    Optional<UsuarioModel> findByNmCpf(String nmCpf);

    @Query("select u from UsuarioModel u where u.flAtivo = true")
    List<UsuarioModel> findAllByFlAtivo();
}