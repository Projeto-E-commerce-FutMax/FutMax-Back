package com.trier.futmax.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "TBPRODUTO")

public class ProdutoModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column(name = "CDPRODUTO")
    private long cdProduto;

    @Column(name = "NMPRODUTO")
    private String nmProduto;

    @Column(name = "DSPRODUTO")
    private String dsProduto;

    @Column(name = "VLPRODUTO")
    private double vlProduto;

    @Column(name = "FLATIVO" )
    private String flAtivo = "Sim";

}
