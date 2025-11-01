package com.trier.futmax.service;

import com.trier.futmax.dto.request.ItemPedidoRequestDTO;
import com.trier.futmax.dto.request.PedidoRequestDTO;
import com.trier.futmax.dto.response.ItemPedidoResponseDTO;
import com.trier.futmax.dto.response.PedidoResponseDTO;
import com.trier.futmax.model.ItemPedidoModel;
import com.trier.futmax.model.PedidoModel;
import com.trier.futmax.model.ProdutoModel;
import com.trier.futmax.model.UsuarioModel;
import com.trier.futmax.repository.ItemPedidoRepository;
import com.trier.futmax.repository.PedidoRepository;
import com.trier.futmax.repository.ProdutoRepository;
import com.trier.futmax.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ItemPedidoRepository itemPedidoRepository;
    private final UsuarioRepository usuarioRepository;
    private final ProdutoRepository produtoRepository;
    private final EstoqueService estoqueService;

    @Transactional
    public PedidoResponseDTO criarPedido(PedidoRequestDTO pedidoRequest) {

        UsuarioModel usuario = usuarioRepository.findById(pedidoRequest.cdUsuario())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado!"));

        PedidoModel pedido = new PedidoModel();
        pedido.setUsuario(usuario);
        pedido.setDtPedido(LocalDateTime.now());
        pedido.setFlAtivo(true);
        pedido.setItens(new ArrayList<>());

        Double vlTotalItens = 0.0;

        for (ItemPedidoRequestDTO itemDTO : pedidoRequest.itens()) {

            ProdutoModel produto = produtoRepository.findById(itemDTO.cdProduto())
                    .orElseThrow(() -> new RuntimeException("Produto não encontrado: " + itemDTO.cdProduto()));

            if (!produto.getFlAtivo()) {
                throw new RuntimeException("Produto inativo: " + produto.getNmProduto());
            }

            if (!estoqueService.temEstoque(itemDTO.cdProduto(), itemDTO.qtItem())) {
                throw new RuntimeException("Estoque insuficiente para: " + produto.getNmProduto());
            }

            ItemPedidoModel item = new ItemPedidoModel();
            item.setPedido(pedido);
            item.setProduto(produto);
            item.setQtItem(itemDTO.qtItem());
            item.setVlUnitario(produto.getVlProduto());
            item.setVlTotal(produto.getVlProduto() * itemDTO.qtItem());
            item.setFlAtivo(true);

            estoqueService.baixarEstoque(itemDTO.cdProduto(), itemDTO.qtItem());

            pedido.getItens().add(item);
            vlTotalItens += item.getVlTotal();
        }

        Double vlFrete = calcularFrete(vlTotalItens);
        pedido.setVlItens(vlTotalItens);
        pedido.setVlFrete(vlFrete);
        pedido.setVlTotalPedido(vlTotalItens + vlFrete);

        pedido = pedidoRepository.save(pedido);

        return new PedidoResponseDTO(
                pedido.getCdPedido(),
                pedido.getUsuario().getCdUsuario(),
                pedido.getUsuario().getNmUsuario(),
                pedido.getVlItens(),
                pedido.getVlFrete(),
                pedido.getVlTotalPedido(),
                pedido.getDtPedido(),
                pedido.getFlAtivo()
        );
    }

    private Double calcularFrete(Double vlItens) {
        if (vlItens >= 200.0) {
            return 0.0;
        } else if (vlItens >= 100.0) {
            return 10.0;
        } else {
            return 20.0;
        }
    }

    @Transactional
    public PedidoResponseDTO buscarPedido(Long cdPedido) {
        PedidoModel pedido = pedidoRepository.findByCdPedido(cdPedido)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado para o ID " + cdPedido));

        return convertToResponseDTO(pedido);
    }

    @Transactional
    public List<ItemPedidoResponseDTO> buscarItensPedido(Long cdPedido) {
        pedidoRepository.findByCdPedido(cdPedido)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));

        List<ItemPedidoModel> itens = itemPedidoRepository.findByPedido_CdPedido(cdPedido);

        return itens.stream()
                .map(item -> new ItemPedidoResponseDTO(
                        item.getCdItemPedido(),
                        item.getPedido().getCdPedido(),
                        item.getProduto().getCdProduto(),
                        item.getProduto().getNmProduto(),
                        item.getQtItem(),
                        item.getVlUnitario(),
                        item.getVlTotal(),
                        item.getFlAtivo()
                ))
                .toList();
    }

    @Transactional
    public List<PedidoResponseDTO> buscarTodos() {
        List<PedidoModel> pedidos = pedidoRepository.findAll();
        return pedidos.stream()
                .map(this::convertToResponseDTO)
                .toList();
    }

    private PedidoResponseDTO convertToResponseDTO(PedidoModel pedido) {
        return new PedidoResponseDTO(
                pedido.getCdPedido(),
                pedido.getUsuario().getCdUsuario(),
                pedido.getUsuario().getNmUsuario(),
                pedido.getVlItens(),
                pedido.getVlFrete(),
                pedido.getVlTotalPedido(),
                pedido.getDtPedido(),
                pedido.getFlAtivo()
        );
    }
    public List<PedidoResponseDTO> buscarPedidosPorUsuario(Long cdUsuario) {
        List<PedidoModel> pedidos = pedidoRepository.findByUsuarioCdUsuarioAndFlAtivoTrue(cdUsuario);

        return pedidos.stream()
                .map(pedido -> {
                    List<ItemPedidoResponseDTO> itens = pedido.getItens().stream()
                            .map(item -> new ItemPedidoResponseDTO(
                                    item.getCdItemPedido(),
                                    item.getPedido().getCdPedido(),
                                    item.getProduto().getCdProduto(),
                                    item.getProduto().getNmProduto(),
                                    item.getQtItem(),
                                    item.getVlUnitario(),
                                    item.getVlTotal(),
                                    item.getFlAtivo()
                            ))
                            .toList();

                    return new PedidoResponseDTO(
                            pedido.getCdPedido(),
                            pedido.getUsuario().getCdUsuario(),
                            pedido.getUsuario().getNmUsuario(),
                            pedido.getVlItens(),
                            pedido.getVlFrete(),
                            pedido.getVlTotalPedido(),
                            pedido.getDtPedido(),
                            pedido.getFlAtivo(),
                            itens
                    );
                })
                .toList();
    }
}