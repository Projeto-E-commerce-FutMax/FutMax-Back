package com.trier.futmax.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

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
    private Boolean flAtivo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CDPRODUTO", referencedColumnName = "CDPRODUTO")
    private ProdutoModel produto;

    public Integer getCdEstoque() {
        return cdEstoque;
    }

    public String getCdLocalEstoque() {
        return cdLocalEstoque;
    }

    public Integer getQtEstoque() {
        return qtEstoque;
    }

    public Boolean getFlAtivo() {
        return flAtivo;
    }

    public ProdutoModel getProduto() {
        return produto;
    }

    public void setCdEstoque(Integer cdEstoque) {
        this.cdEstoque = cdEstoque;
    }

    public void setCdLocalEstoque(String cdLocalEstoque) {
        this.cdLocalEstoque = cdLocalEstoque;
    }

    public void setQtEstoque(Integer qtEstoque) {
        this.qtEstoque = qtEstoque;
    }

    public void setFlAtivo(Boolean flAtivo) {
        this.flAtivo = flAtivo;
    }

    public void setProduto(ProdutoModel produto) {
        this.produto = produto;
    }
}
