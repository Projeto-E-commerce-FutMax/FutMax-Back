package com.trier.futmax.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})

public class ProdutoModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CDPRODUTO")
    private Long cdProduto;

    @Column(name = "NMPRODUTO" , unique = true)
    private String nmProduto;

    @Column(name = "DSPRODUTO")
    private String dsProduto;

    @Column(name = "VLPRODUTO")
    private Double vlProduto;

    @Column(name = "FLATIVO" )
    private Boolean flAtivo;

    @Column(name = "IMGURL")
    private String imgUrl;

}
