package com.essenciadafruta.model;

/**
 * Classe que representa itens de Estoque (matérias-primas)
 */
public class Estoque {
    private int id;
    private String item; // Fruta, Embalagem, etc
    private String descricao;
    private double quantidade; // em kg ou unidades
    private String unidade; // kg, litros, unidades
    private double preco; // preço unitário
    private String fornecedor;
    
    public Estoque() {
    }
    
    public Estoque(String item, String descricao, double quantidade, String unidade, double preco, String fornecedor) {
        this.item = item;
        this.descricao = descricao;
        this.quantidade = quantidade;
        this.unidade = unidade;
        this.preco = preco;
        this.fornecedor = fornecedor;
    }
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getItem() {
        return item;
    }
    
    public void setItem(String item) {
        this.item = item;
    }
    
    public String getDescricao() {
        return descricao;
    }
    
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
    
    public double getQuantidade() {
        return quantidade;
    }
    
    public void setQuantidade(double quantidade) {
        this.quantidade = quantidade;
    }
    
    public String getUnidade() {
        return unidade;
    }
    
    public void setUnidade(String unidade) {
        this.unidade = unidade;
    }
    
    public double getPreco() {
        return preco;
    }
    
    public void setPreco(double preco) {
        this.preco = preco;
    }
    
    public String getFornecedor() {
        return fornecedor;
    }
    
    public void setFornecedor(String fornecedor) {
        this.fornecedor = fornecedor;
    }
    
    public double getValorTotal() {
        return quantidade * preco;
    }
    
    @Override
    public String toString() {
        return String.format("Estoque{id=%d, item='%s', qtd=%.2f %s, valor=R$%.2f}", 
                id, item, quantidade, unidade, getValorTotal());
    }
}
