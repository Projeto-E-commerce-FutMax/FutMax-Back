package com.trier.futmax.repository;


import com.trier.futmax.model.ProdutoModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProdutoRepository extends JpaRepository<ProdutoModel, Integer> {


    Optional<ProdutoModel> findById(int id);
    Optional<ProdutoModel> findByNmProduto(String nmProduto);


}
