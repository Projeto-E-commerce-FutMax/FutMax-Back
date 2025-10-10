package com.trier.futmax.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "TBESTOQUE")
@Table(name = "TBESTOQUE")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter

public class EstoqueModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CDESTOQUE")
    private Integer cdEstoque;

    @Column(name = "CDLOCALESTOQUE")
    private String cdLocalEstoque;

    @Column(name = "QTESTOQUE")
    private Integer qtEstoque;

    @Column(name = "FLATIVO")
    private String flAtivo = "";

    //@OneToMany
    //@JoinColumn(name = "CDPRODUTO")
    //private List<ProdutoModel> cdProduto;


}
