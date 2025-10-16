package com.trier.futmax.repository;

import com.trier.futmax.model.ItemPedidoModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemPedidoRepository extends JpaRepository<ItemPedidoModel, Long> {

    List<ItemPedidoModel> findByPedido_CdPedido(Long cdPedido);
}