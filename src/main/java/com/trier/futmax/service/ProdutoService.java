package com.trier.futmax.service;

import com.trier.futmax.dto.request.ProdutoRequestDTO;
import com.trier.futmax.dto.response.ProdutoResponseDTO;
import com.trier.futmax.model.EstoqueModel;
import com.trier.futmax.model.ProdutoModel;
import com.trier.futmax.repository.ProdutoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ProdutoService {

    public final ProdutoRepository produtoRepository;

    public ProdutoResponseDTO cadastrarProduto(ProdutoRequestDTO produtoRequestDTO) {
        var produto = produtoRepository.save(new ProdutoModel(
                null,
                produtoRequestDTO.nmProduto(),
                produtoRequestDTO.dsProduto(),
                produtoRequestDTO.vlProduto(),
                true
        ));
        return  new ProdutoResponseDTO(
                produto.getCdProduto(),
                produto.getNmProduto(),
                produto.getDsProduto(),
                produto.getVlProduto(),
                produto.getFlAtivo()
        );
    }
    public ProdutoResponseDTO consultarProduto(Long cdProduto) {
        ProdutoModel produto = produtoRepository.findByCdProduto(cdProduto)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado para o ID: " + cdProduto));

        return new ProdutoResponseDTO(
                produto.getCdProduto(),
                produto.getNmProduto(),
                produto.getDsProduto(),
                produto.getVlProduto(),
                produto.getFlAtivo()
        );
    }

    public ProdutoResponseDTO atualizarProduto(ProdutoRequestDTO produtoRequestDTO) {
        ProdutoModel produto = produtoRepository.findByCdProduto(produtoRequestDTO.cdProduto())
                .orElseThrow(() -> new RuntimeException("Produto não encontrado para o ID: " + produtoRequestDTO.cdProduto()));

        produto.setNmProduto(produtoRequestDTO.nmProduto());
        produto.setDsProduto(produtoRequestDTO.dsProduto());
        produto.setVlProduto(produtoRequestDTO.vlProduto());
        produto.setFlAtivo(produtoRequestDTO.flAtivo());

        produtoRepository.save(produto);

        return new ProdutoResponseDTO(
                produto.getCdProduto(),
                produto.getNmProduto(),
                produto.getDsProduto(),
                produto.getVlProduto(),
                produto.getFlAtivo()
        );
    }

    public List<ProdutoModel> consultarTodos() {

        return produtoRepository.findAll();
    }

    public void desativarProduto(Long cdProduto) {
        ProdutoModel produto = produtoRepository.findByCdProduto(cdProduto)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado para o ID: " + cdProduto));

        produto.setFlAtivo(false);
        produtoRepository.save(produto);

    }



}
