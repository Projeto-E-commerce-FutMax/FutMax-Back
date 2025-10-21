package com.trier.futmax.repository;

import com.trier.futmax.model.ProdutoModel;
import com.trier.futmax.model.UsuarioModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UsuarioRepository extends JpaRepository<UsuarioModel, Long> {

    @Query("select u from UsuarioModel u where u.flAtivo = true")
    List<UsuarioModel> findAllByFlAtivo();
}

