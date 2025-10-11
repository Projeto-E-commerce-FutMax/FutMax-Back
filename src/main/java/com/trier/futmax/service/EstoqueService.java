package com.trier.futmax.service;

import com.trier.futmax.dto.response.EstoqueResponseDTO;
import com.trier.futmax.model.EstoqueModel;
import com.trier.futmax.repository.EstoqueRepository;
import com.trier.futmax.repository.ProdutoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EstoqueService {

    @Autowired
    private EstoqueRepository estoqueRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Transactional
    public EstoqueResponseDTO consultarEstoque(Integer cdEstoque) {
        EstoqueModel estoque = estoqueRepository.findByCdEstoque(cdEstoque)
                .orElseThrow(() -> new RuntimeException("Estoque não encontrado para o ID: " + cdEstoque));

        return new EstoqueResponseDTO(
                estoque.getCdEstoque(),
                estoque.getCdLocalEstoque(),
                estoque.getQtEstoque(),
                estoque.getFlAtivo(),
                estoque.getProduto() != null ? estoque.getProduto().getCdProduto() : null
        );
    }

    @Transactional
    public List<EstoqueModel> consultarTodos() {
        return estoqueRepository.findAll();
    }
}
