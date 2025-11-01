package com.trier.futmax.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "TBPEDIDO")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter

public class PedidoModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CDPEDIDO")
    private Long cdPedido;

    @Column(name = "VLITENS")
    private Double vlItens;

    @Column(name = "VLFRETE")
    private Double vlFrete;

    @Column(name = "VLTOTALPEDIDO")
    private Double vlTotalPedido;

    @CreationTimestamp
    @Column(name = "DTPEDIDO")
    private LocalDateTime dtPedido;

    @Column(name = "FLATIVO")
    private Boolean flAtivo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CDUSUARIO", referencedColumnName = "CDUSUARIO")
    private UsuarioModel usuario;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemPedidoModel> itens = new ArrayList<>();

}