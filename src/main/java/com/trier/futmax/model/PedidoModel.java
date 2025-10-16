package com.trier.futmax.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity(name = "TBPEDIDO")
@Table(name = "TBPEDIDO")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter

public class PedidoModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CDPRODUTO")
    private Long cdPedido;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CDUSUARIO", referencedColumnName = "CDUSUARIO")
    private UsuarioModel usuario;

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

}
