package com.trier.futmax.service;

import com.trier.futmax.dto.request.ProdutoRequestDTO;
import com.trier.futmax.dto.response.ProdutoResponseDTO;
import com.trier.futmax.model.ProdutoModel;
import com.trier.futmax.repository.ProdutoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ProdutoService {

    public final ProdutoRepository produtoRepository;
    public final EstoqueService estoqueService;

    public ProdutoResponseDTO cadastrarProduto(ProdutoRequestDTO produtoRequestDTO) {
        var produto = produtoRepository.save(new ProdutoModel(
                null,
                produtoRequestDTO.nmProduto(),
                produtoRequestDTO.dsProduto(),
                produtoRequestDTO.vlProduto(),
                true,
                produtoRequestDTO.imgUrl(),
                produtoRequestDTO.nmCategoria()
        ));
        return convertToResponseDTO(produto);
    }
    public ProdutoResponseDTO consultarProduto(Long cdProduto) {
        ProdutoModel produto = produtoRepository.findByCdProduto(cdProduto)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado para o ID: " + cdProduto));

        return convertToResponseDTO(produto);
    }

    public ProdutoResponseDTO atualizarProduto(Long cdProduto, ProdutoRequestDTO produtoRequestDTO) {
        ProdutoModel produto = produtoRepository.findById(cdProduto)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado para o ID: " + cdProduto));

        produto.setNmProduto(produtoRequestDTO.nmProduto());
        produto.setDsProduto(produtoRequestDTO.dsProduto());
        produto.setVlProduto(produtoRequestDTO.vlProduto());
        produto.setFlAtivo(produtoRequestDTO.flAtivo());
        produto.setNmCategoria(produtoRequestDTO.nmCategoria());
        if (produtoRequestDTO.imgUrl() != null && !produtoRequestDTO.imgUrl().isBlank()) {
            produto.setImgUrl(produtoRequestDTO.imgUrl());
        }

        produtoRepository.save(produto);

        return convertToResponseDTO(produto);
    }


    public List<ProdutoResponseDTO> buscarTodosAtivos() {
        List<ProdutoModel> produto = produtoRepository.findAllByFlAtivo();
        return produto.stream().map(this:: convertToResponseDTO).toList();
    }

    public List<ProdutoResponseDTO> buscarTodos() {
        List<ProdutoModel> produto = produtoRepository.findAll();
        return produto.stream().map(this:: convertToResponseDTO).toList();
    }
    private ProdutoResponseDTO convertToResponseDTO(ProdutoModel produto) {
        // Buscar estoque do produto
        Integer qtEstoque = 0;
        try {
            List<com.trier.futmax.dto.response.EstoqueResponseDTO> estoques = estoqueService.consultarEstoquesPorProduto(produto.getCdProduto());
            qtEstoque = estoques.stream()
                    .filter(e -> e.flAtivo())
                    .mapToInt(e -> e.qtEstoque() != null ? e.qtEstoque() : 0)
                    .sum();
        } catch (Exception e) {
            // Se não encontrar estoque, mantém 0
            qtEstoque = 0;
        }
        
        return new ProdutoResponseDTO(
                produto.getCdProduto(),
                produto.getNmProduto(),
                produto.getDsProduto(),
                produto.getVlProduto(),
                produto.getFlAtivo(),
                produto.getImgUrl(),
                qtEstoque,
                produto.getNmCategoria()
        );
    }

    public ProdutoResponseDTO desativarProduto(Long cdProduto) {
        ProdutoModel produto = produtoRepository.findByCdProduto(cdProduto)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado para o ID: " + cdProduto));

        produto.setFlAtivo(false);
        produtoRepository.save(produto);

        return convertToResponseDTO(produto);
    }

    public ProdutoResponseDTO reativarProduto(Long cdProduto) {
        ProdutoModel produto = produtoRepository.findByCdProduto(cdProduto)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado para o ID: " + cdProduto));

        produto.setFlAtivo(true);
        produtoRepository.save(produto);

        return convertToResponseDTO(produto);
    }



}
