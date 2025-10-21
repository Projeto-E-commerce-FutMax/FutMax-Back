package com.trier.futmax.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

@Entity(name = "TBESTOQUE")
@Table(name = "TBESTOQUE")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})

public class EstoqueModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CDESTOQUE")
    private Long cdEstoque;

    @Column(name = "QTESTOQUE")
    private Integer qtEstoque;

    @Column(name = "FLATIVO")
    private Boolean flAtivo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CDPRODUTO", referencedColumnName = "CDPRODUTO")
    private ProdutoModel produto;


}
