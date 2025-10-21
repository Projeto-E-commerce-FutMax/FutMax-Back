package com.trier.futmax.repository;

import com.trier.futmax.model.EstoqueModel;
import com.trier.futmax.model.ProdutoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EstoqueRepository extends JpaRepository<EstoqueModel, Long> {

    @Query("select p from EstoqueModel p where p.flAtivo = true")
    List<EstoqueModel> findAllByFlAtivo();
}
