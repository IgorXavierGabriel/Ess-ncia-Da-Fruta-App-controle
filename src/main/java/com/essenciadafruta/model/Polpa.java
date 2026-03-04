package com.essenciadafruta.model;

import java.time.LocalDate;

/**
 * Classe que representa um produto de Polpa Natural
 */
public class Polpa {
    private int id;
    private String sabor;
    private double peso; // em kg
    private int quantidade; // quantidade de unidades
    private double preco; // preço por unidade
    private LocalDate dataProducao;
    private String lote;
    
    public Polpa() {
    }
    
    public Polpa(String sabor, double peso, int quantidade, double preco) {
        this.sabor = sabor;
        this.peso = peso;
        this.quantidade = quantidade;
        this.preco = preco;
        this.dataProducao = LocalDate.now();
    }
    
    public Polpa(String sabor, double peso, int quantidade, double preco, LocalDate dataProducao, String lote) {
        this.sabor = sabor;
        this.peso = peso;
        this.quantidade = quantidade;
        this.preco = preco;
        this.dataProducao = dataProducao;
        this.lote = lote;
    }
    
    public Polpa(int id, String sabor, double peso, int quantidade, double preco, LocalDate dataProducao, String lote) {
        this.id = id;
        this.sabor = sabor;
        this.peso = peso;
        this.quantidade = quantidade;
        this.preco = preco;
        this.dataProducao = dataProducao;
        this.lote = lote;
    }
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }

    public String getSabor() {
        return sabor;
    }

    public void setSabor(String sabor) {
        this.sabor = sabor;
    }

    public double getPeso() {
        return peso;
    }

    public void setPeso(double peso) {
        this.peso = peso;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }
    
    public LocalDate getDataProducao() {
        return dataProducao;
    }
    
    public void setDataProducao(LocalDate dataProducao) {
        this.dataProducao = dataProducao;
    }
    
    public String getLote() {
        return lote;
    }
    
    public void setLote(String lote) {
        this.lote = lote;
    }
    
    public double getValorTotal() {
        return quantidade * preco;
    }

    @Override
    public String toString() {
        return String.format("Polpa{id=%d, sabor='%s', peso=%.2f kg, qtd=%d, preco=R$%.2f, data=%s, lote='%s'}", 
                id, sabor, peso, quantidade, preco, dataProducao, lote);
    }
}
