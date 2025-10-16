package com.trier.futmax.service;

import com.trier.futmax.dto.request.PedidoRequestDTO;
import com.trier.futmax.dto.response.PedidoResponseDTO;
import com.trier.futmax.model.PedidoModel;
import com.trier.futmax.model.UsuarioModel;
import com.trier.futmax.repository.PedidoRepository;
import com.trier.futmax.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Transactional
    public PedidoResponseDTO criarPedido(PedidoRequestDTO pedidoRequest) {

        UsuarioModel usuario = usuarioRepository.findById(pedidoRequest.cdUsuario())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado!"));

        var pedido = new PedidoModel();
        pedido.setUsuario(usuario);
        pedido.setVlItens(pedidoRequest.vlItens());
        pedido.setVlFrete(pedidoRequest.vlFrete());
        pedido.setVlTotalPedido(pedidoRequest.vlTotalPedido());
        pedido.setDtPedido(pedidoRequest.dtPedido());
        pedido.setFlAtivo(pedidoRequest.flAtivo());
        pedidoRepository.save(pedido);

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

    @Transactional
    public PedidoResponseDTO buscarPedido(Long cdPedido) {
        PedidoModel pedido = pedidoRepository.findByCdPedido(cdPedido)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado para o ID " + cdPedido));

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
}
