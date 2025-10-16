package com.trier.futmax.service;

import com.trier.futmax.dto.request.ProdutoRequestDTO;
import com.trier.futmax.dto.response.PedidoResponseDTO;
import com.trier.futmax.dto.response.ProdutoResponseDTO;
import com.trier.futmax.model.PedidoModel;
import com.trier.futmax.model.ProdutoModel;
import com.trier.futmax.repository.ProdutoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

import static java.util.stream.Collectors.toList;

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

    public ProdutoResponseDTO atualizarProduto(Long cdProduto, ProdutoRequestDTO produtoRequestDTO) {
        ProdutoModel produto = produtoRepository.findById(cdProduto)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado para o ID: " + cdProduto));

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

    public List<ProdutoResponseDTO> buscarTodos() {
        List<ProdutoModel> produto = produtoRepository.findAll();
        return produto.stream()
                .map(this:: convertToResponseDTO)
                .toList();
    }
    private ProdutoResponseDTO convertToResponseDTO(ProdutoModel pedido) {
        return new ProdutoResponseDTO(
                pedido.getCdProduto(),
                pedido.getNmProduto(),
                pedido.getDsProduto(),
                pedido.getVlProduto(),
                pedido.getFlAtivo()
        );
    }

    public ProdutoResponseDTO desativarProduto(Long cdProduto) {
        ProdutoModel produto = produtoRepository.findByCdProduto(cdProduto)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado para o ID: " + cdProduto));

        produto.setFlAtivo(false);
        produtoRepository.save(produto);

        return new ProdutoResponseDTO(
                produto.getCdProduto(),
                produto.getNmProduto(),
                produto.getDsProduto(),
                produto.getVlProduto(),
                produto.getFlAtivo()
        );

    }

    public ProdutoResponseDTO reativarProduto(Long cdProduto) {
        ProdutoModel produto = produtoRepository.findByCdProduto(cdProduto)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado para o ID: " + cdProduto));

        produto.setFlAtivo(true);
        produtoRepository.save(produto);

        return new ProdutoResponseDTO(
                produto.getCdProduto(),
                produto.getNmProduto(),
                produto.getDsProduto(),
                produto.getVlProduto(),
                produto.getFlAtivo()
        );

    }



}
