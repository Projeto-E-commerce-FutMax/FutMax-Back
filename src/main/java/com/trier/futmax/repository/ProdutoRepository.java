package com.trier.futmax.repository;

import com.trier.futmax.model.ProdutoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProdutoRepository extends JpaRepository<ProdutoModel, Long> {

    Optional<ProdutoModel> findByCdProduto(Long cdProduto);


    Optional<ProdutoModel> findByNmProduto(String nmProduto);

    @Query("select u from ProdutoModel u where u.flAtivo = true")
    List<ProdutoModel> findAllByFlAtivo();

}
