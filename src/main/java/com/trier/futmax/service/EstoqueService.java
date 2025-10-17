package com.trier.futmax.service;

import com.trier.futmax.dto.request.EstoqueRequestDTO;
import com.trier.futmax.dto.response.EstoqueResponseDTO;
import com.trier.futmax.model.EstoqueModel;
import com.trier.futmax.model.ProdutoModel;
import com.trier.futmax.repository.EstoqueRepository;
import com.trier.futmax.repository.ProdutoRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EstoqueService {

    private final EstoqueRepository estoqueRepository;
    private final ProdutoRepository produtoRepository;

    @Transactional
    public  EstoqueResponseDTO cadastrarEstoque(EstoqueRequestDTO estoqueRequest) {

        ProdutoModel produto = null;
        if (estoqueRequest.cdProduto() != null) {
            produto = produtoRepository.findById(estoqueRequest.cdProduto())
                    .orElseThrow(() -> new EntityNotFoundException("Produto não encontrado!"));
        }

        var estoque = new EstoqueModel();
        estoque.setCdLocalEstoque(estoqueRequest.cdLocalEstoque());
        estoque.setQtEstoque(estoqueRequest.qtEstoque());
        estoque.setFlAtivo(true);
        estoque.setProduto(produto);

        estoqueRepository.save(estoque);

        return new EstoqueResponseDTO(
                estoque.getCdEstoque(),
                estoque.getCdLocalEstoque(),
                estoque.getQtEstoque(),
                estoque.getFlAtivo(),
                estoque.getProduto() != null ? estoque.getProduto().getCdProduto() : null
        );
    }

    @Transactional
    public EstoqueResponseDTO consultarEstoque(Long cdEstoque) {

        EstoqueModel estoque = estoqueRepository.findById(cdEstoque)
                .orElseThrow(() -> new RuntimeException("Estoque não encontrado para o ID: " + cdEstoque));

        return new EstoqueResponseDTO(
                estoque.getCdEstoque(),
                estoque.getCdLocalEstoque(),
                estoque.getQtEstoque(),
                estoque.getFlAtivo(),
                estoque.getProduto() != null ? estoque.getProduto().getCdProduto() : null
        );
    }

    public List<EstoqueModel> consultarTodos() {
        return estoqueRepository.findAll();
    }

    @Transactional
    public EstoqueResponseDTO atualizarEstoque(Long cdEstoque, EstoqueRequestDTO estoqueRequest) {

        EstoqueModel estoque = estoqueRepository.findById(cdEstoque)
                .orElseThrow(() -> new RuntimeException("Estoque não encontrado para o ID: " + cdEstoque));

        estoque.setCdLocalEstoque(estoqueRequest.cdLocalEstoque());
        estoque.setQtEstoque(estoqueRequest.qtEstoque());

        estoqueRepository.save(estoque);

        return new EstoqueResponseDTO(
                estoque.getCdEstoque(),
                estoque.getCdLocalEstoque(),
                estoque.getQtEstoque(),
                estoque.getFlAtivo(),
                estoque.getProduto() != null ? estoque.getProduto().getCdProduto() : null
        );
    }

    @Transactional
    public void removerEstoque(Long cdEstoque) {

        EstoqueModel estoque = estoqueRepository.findById(cdEstoque)
                .orElseThrow(() -> new RuntimeException("Estoque não encontrado para o ID: " + cdEstoque));

        estoqueRepository.delete(estoque);
    }


    public boolean temEstoque(Long cdProduto, Integer quantidade) {
        List<EstoqueModel> estoques = estoqueRepository.findAll();

        //Somatodo o estoque disponível deste produto em todos os locais
        int estoqueTotal = estoques.stream()
                .filter(e -> e.getProduto() != null &&
                        e.getProduto().getCdProduto().equals(cdProduto) &&
                        e.getFlAtivo())
                .mapToInt(EstoqueModel::getQtEstoque)
                .sum();

        return estoqueTotal >= quantidade;
    }

    public Integer consultarQuantidadeDisponivel(Long cdProduto) {
        List<EstoqueModel> estoques = estoqueRepository.findAll();

        return estoques.stream()
                .filter(e -> e.getProduto() != null &&
                        e.getProduto().getCdProduto().equals(cdProduto) &&
                        e.getFlAtivo())
                .mapToInt(EstoqueModel::getQtEstoque)
                .sum();
    }

    @Transactional
    public void baixarEstoque(Long cdProduto, Integer quantidade) {
        List<EstoqueModel> estoques = estoqueRepository.findAll();

        int quantidadeRestante = quantidade;

        // Itera pelos estoques e vai baixando até completar a quantidade
        for (EstoqueModel estoque : estoques) {
            if (quantidadeRestante <= 0) {
                break; // Quantidade completa já foi baixada
            }

            // Verifica se é o produto correto, está ativo e tem quantidade
            if (estoque.getProduto() != null &&
                    estoque.getProduto().getCdProduto().equals(cdProduto) &&
                    estoque.getFlAtivo() &&
                    estoque.getQtEstoque() > 0) {

                int qtDisponivel = estoque.getQtEstoque();
                int qtBaixar = Math.min(qtDisponivel, quantidadeRestante);

                // Atualiza a quantidade do estoque
                estoque.setQtEstoque(qtDisponivel - qtBaixar);
                estoqueRepository.save(estoque);

                quantidadeRestante -= qtBaixar;
            }
        }

        // Se ainda sobrou quantidade, significa que não tinha estoque suficiente
        if (quantidadeRestante > 0) {
            throw new RuntimeException("Estoque insuficiente para o produto código: " + cdProduto);
        }
    }

    @Transactional
    public EstoqueResponseDTO adicionarEstoque(Long cdProduto, String cdLocalEstoque, Integer quantidade) {

        // Buscar produto
        ProdutoModel produto = produtoRepository.findById(cdProduto)
                .orElseThrow(() -> new EntityNotFoundException("Produto não encontrado!"));

        // Buscar ou criar estoque para este produto neste local
        List<EstoqueModel> estoques = estoqueRepository.findAll();

        EstoqueModel estoque = estoques.stream()
                .filter(e -> e.getProduto() != null &&
                        e.getProduto().getCdProduto().equals(cdProduto) &&
                        e.getCdLocalEstoque().equals(cdLocalEstoque) &&
                        e.getFlAtivo())
                .findFirst()
                .orElse(null);

        if (estoque == null) {
            // Criar novo estoque
            estoque = new EstoqueModel();
            estoque.setProduto(produto);
            estoque.setCdLocalEstoque(cdLocalEstoque);
            estoque.setQtEstoque(quantidade);
            estoque.setFlAtivo(true);
        } else {
            // Adicionar ao estoque existente
            estoque.setQtEstoque(estoque.getQtEstoque() + quantidade);
        }

        estoqueRepository.save(estoque);

        return new EstoqueResponseDTO(
                estoque.getCdEstoque(),
                estoque.getCdLocalEstoque(),
                estoque.getQtEstoque(),
                estoque.getFlAtivo(),
                estoque.getProduto().getCdProduto()
        );
    }


    public List<EstoqueResponseDTO> consultarEstoquesPorProduto(Long cdProduto) {
        List<EstoqueModel> estoques = estoqueRepository.findAll();

        return estoques.stream()
                .filter(e -> e.getProduto() != null &&
                        e.getProduto().getCdProduto().equals(cdProduto))
                .map(e -> new EstoqueResponseDTO(
                        e.getCdEstoque(),
                        e.getCdLocalEstoque(),
                        e.getQtEstoque(),
                        e.getFlAtivo(),
                        e.getProduto().getCdProduto()
                ))
                .toList();
    }

    @Transactional
    public EstoqueResponseDTO desativarEstoque(Long cdEstoque) {
        EstoqueModel estoque = estoqueRepository.findById(cdEstoque)
                .orElseThrow(() -> new RuntimeException("Estoque não encontrado para o ID: " + cdEstoque));

        estoque.setFlAtivo(false);
        estoqueRepository.save(estoque);

        return new EstoqueResponseDTO(
                estoque.getCdEstoque(),
                estoque.getCdLocalEstoque(),
                estoque.getQtEstoque(),
                estoque.getFlAtivo(),
                estoque.getProduto() != null ? estoque.getProduto().getCdProduto() : null
        );
    }

    @Transactional
    public EstoqueResponseDTO reativarEstoque(Long cdEstoque) {
        EstoqueModel estoque = estoqueRepository.findById(cdEstoque)
                .orElseThrow(() -> new RuntimeException("Estoque não encontrado para o ID: " + cdEstoque));

        estoque.setFlAtivo(true);
        estoqueRepository.save(estoque);

        return new EstoqueResponseDTO(
                estoque.getCdEstoque(),
                estoque.getCdLocalEstoque(),
                estoque.getQtEstoque(),
                estoque.getFlAtivo(),
                estoque.getProduto() != null ? estoque.getProduto().getCdProduto() : null
        );
    }
}