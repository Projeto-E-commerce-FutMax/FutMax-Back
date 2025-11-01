package com.trier.futmax.repository;

import com.trier.futmax.model.PedidoModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PedidoRepository extends JpaRepository<PedidoModel, Long> {

    Optional<PedidoModel> findByCdPedido(Long cdPedido);
    List<PedidoModel> findByUsuarioCdUsuario(Long cdUsuario);
    List<PedidoModel> findByUsuarioCdUsuarioAndFlAtivoTrue(Long cdUsuario);

}
