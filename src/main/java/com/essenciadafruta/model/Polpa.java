package com.essenciadafruta.model;

import java.time.LocalDate;

/**
 * Classe que representa um produto de Polpa Natural
 */
public class Polpa {
    private int id;
    private String sabor;
    private double peso; // em kg
    private int quantidade; // quantidade de unidades (estoque atual)
    private int quantidadeProduzida; // quantidade original produzida (nunca muda)
    private double preco; // preço por unidade (padrão 0 para produção)
    private String unidade;             // 170g, 1kg etc.
    private LocalDate dataProducao;
    private LocalDate dataValidade;
    private String lote;
    
    public Polpa() {
    }
    
    // utilizado ao cadastrar nova produção (preço fornecido separadamente ou zero)
    public Polpa(String sabor, double peso, String unidade, int quantidade,
                 double preco, LocalDate dataProducao, LocalDate dataValidade, String lote) {
        this.sabor = sabor;
        this.peso = peso;
        this.unidade = unidade;
        this.quantidade = quantidade;
        this.quantidadeProduzida = quantidade; // guarda o valor original
        this.preco = preco;
        this.dataProducao = dataProducao;
        this.dataValidade = dataValidade;
        this.lote = lote;
    }
    
    // construtor completo com id (para leitura do banco)
    public Polpa(int id, String sabor, double peso, String unidade, int quantidade, int quantidadeProduzida,
                 double preco, LocalDate dataProducao, LocalDate dataValidade, String lote) {
        this.id = id;
        this.sabor = sabor;
        this.peso = peso;
        this.unidade = unidade;
        this.quantidade = quantidade;
        this.quantidadeProduzida = quantidadeProduzida;
        this.preco = preco;
        this.dataProducao = dataProducao;
        this.dataValidade = dataValidade;
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

    public int getQuantidadeProduzida() {
        return quantidadeProduzida;
    }

    public void setQuantidadeProduzida(int quantidadeProduzida) {
        this.quantidadeProduzida = quantidadeProduzida;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    public String getUnidade() {
        return unidade;
    }

    public void setUnidade(String unidade) {
        this.unidade = unidade;
    }
    
    public LocalDate getDataProducao() {
        return dataProducao;
    }
    
    public void setDataProducao(LocalDate dataProducao) {
        this.dataProducao = dataProducao;
    }
    
    public LocalDate getDataValidade() {
        return dataValidade;
    }
    
    public void setDataValidade(LocalDate dataValidade) {
        this.dataValidade = dataValidade;
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
        return String.format("Polpa{id=%d, sabor='%s', peso=%.2f kg, unidade='%s', qtd=%d, preco=R$%.2f, prod=%s, val=%s, lote='%s'}", 
                id, sabor, peso, unidade, quantidade, preco, dataProducao, dataValidade, lote);
    }
}
