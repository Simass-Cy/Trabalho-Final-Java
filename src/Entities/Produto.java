package Entities;

import Application.TipoDeProduto;

public class Produto {
    private String nomeProduto;
    private String descricaoProduto;
    private float precoProduto;
    private TipoDeProduto Categoria;

    //no args constructor
    public Produto() {
    }

    //all args constructor
    public Produto(String nomeProduto, String descricaoProduto, float precoProduto, TipoDeProduto categoria) {
        this.nomeProduto = nomeProduto;
        this.descricaoProduto = descricaoProduto;
        this.precoProduto = precoProduto;
        Categoria = categoria;
    }

    //gets e sets
    public String getNomeProduto() {
        return nomeProduto;
    }

    public void setNomeProduto(String nomeProduto) {
        this.nomeProduto = nomeProduto;
    }

    public String getDescricaoProduto() {
        return descricaoProduto;
    }

    public void setDescricaoProduto(String descricaoProduto) {
        this.descricaoProduto = descricaoProduto;
    }

    public float getPrecoProduto() {
        return precoProduto;
    }

    public void setPrecoProduto(float precoProduto) {
        this.precoProduto = precoProduto;
    }

    public TipoDeProduto getCategoria() {
        return Categoria;
    }

    public void setCategoria(TipoDeProduto categoria) {
        Categoria = categoria;
    }
}
