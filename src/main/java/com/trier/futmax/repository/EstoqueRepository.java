package com.trier.futmax.repository;

import com.trier.futmax.model.EstoqueModel;
import com.trier.futmax.model.ProdutoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface EstoqueRepository extends JpaRepository<EstoqueModel, Long> {

    @Query("select u from EstoqueModel u where u.flAtivo = true")
    List<EstoqueModel> findAllByFlAtivo();
}
