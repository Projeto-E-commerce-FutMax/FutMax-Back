package com.trier.futmax.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "TBITEMPEDIDO")
@Table(name = "TBITEMPEDIDO")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class ItemPedidoModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CDITEMPEDIDO")
    private Long cdItemPedido;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CDPEDIDO", referencedColumnName = "CDPEDIDO")
    private PedidoModel pedido;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CDPRODUTO", referencedColumnName = "CDPRODUTO")
    private ProdutoModel produto;

    @Column(name = "QTITEM")
    private Integer qtItem;

    @Column(name = "VLUNITARIO")
    private Double vlUnitario;

    @Column(name = "VLTOTAL")
    private Double vlTotal;

    @Column(name = "FLATIVO")
    private Boolean flAtivo;
}