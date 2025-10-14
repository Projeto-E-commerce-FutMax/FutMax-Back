package com.trier.futmax.service;

import com.trier.futmax.dto.request.EstoqueRequestDTO;
import com.trier.futmax.dto.response.EstoqueResponseDTO;
import com.trier.futmax.model.EstoqueModel;
import com.trier.futmax.model.ProdutoModel;
import com.trier.futmax.repository.EstoqueRepository;
import com.trier.futmax.repository.ProdutoRepository;
import jakarta.persistence.EntityNotFoundException;
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
    public EstoqueResponseDTO cadastrarEstoque(EstoqueRequestDTO estoqueRequest) {

        ProdutoModel produto = null;
        if (estoqueRequest.produto() != null) {
            produto = produtoRepository.findById(estoqueRequest.produto().getCdProduto())
                    .orElseThrow(() -> new EntityNotFoundException("Produto não encontrado!"));
        }

        var estoque = new EstoqueModel();
        estoque.setCdLocalEstoque(estoqueRequest.cdLocalEstoque());
        estoque.setQtEstoque(estoqueRequest.qtEstoque());
        estoque.setFlAtivo(true);
        estoque.setProduto(estoqueRequest.produto());

        estoqueRepository.save(estoque);

        return new EstoqueResponseDTO(
                estoque.getCdEstoque(),
                estoque.getCdLocalEstoque(),
                estoque.getQtEstoque(),
                estoque.getFlAtivo(),
                estoque.getProduto()
        );
    }

    @Transactional
    public EstoqueResponseDTO consultarEstoque(Integer cdEstoque) {

        EstoqueModel estoque = estoqueRepository.findByCdEstoque(cdEstoque)
                .orElseThrow(() -> new RuntimeException("Estoque não encontrado para o ID: " + cdEstoque));

        return new EstoqueResponseDTO(
                estoque.getCdEstoque(),
                estoque.getCdLocalEstoque(),
                estoque.getQtEstoque(),
                estoque.getFlAtivo(),
                estoque.getProduto()
        );
    }

    @Transactional
    public List<EstoqueModel> consultarTodos() {

        return estoqueRepository.findAll();
    }

    @Transactional
    public EstoqueResponseDTO atualizarEstoque(Integer cdEstoque, EstoqueRequestDTO estoqueRequest) {

        ProdutoModel produto = null;

        EstoqueModel estoque = estoqueRepository.findByCdEstoque(cdEstoque)
                .orElseThrow(() -> new RuntimeException("Estoque não encontrado para o ID: " + cdEstoque));

        estoque.setCdLocalEstoque(estoqueRequest.cdLocalEstoque());
        estoque.setQtEstoque(estoqueRequest.qtEstoque());

        estoqueRepository.save(estoque);

        return new EstoqueResponseDTO(
                estoque.getCdEstoque(),
                estoque.getCdLocalEstoque(),
                estoque.getQtEstoque(),
                estoque.getFlAtivo(),
                estoque.getProduto()
        );
    }

    @Transactional
    public void removerEstoque(Integer cdEstoque) {

        EstoqueModel estoque = estoqueRepository.findByCdEstoque(cdEstoque)
                .orElseThrow(() -> new RuntimeException("Estoque não encontrado para o ID: " + cdEstoque));

        estoqueRepository.delete(estoque);
    }
}
