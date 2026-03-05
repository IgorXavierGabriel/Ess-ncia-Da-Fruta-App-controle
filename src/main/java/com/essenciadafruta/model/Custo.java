package com.essenciadafruta.model;

import java.time.LocalDate;

/**
 * Modelo para Custos
 */
public class Custo {
    private int id;
    private String tipo; // Imposto, Compra de produtos, Compra de fruta, Gasolina, Energia, etc
    private String descricao;
    private double valor;
    private LocalDate data;
    
    // Construtor para criação
    public Custo(String tipo, String descricao, double valor, LocalDate data) {
        this.tipo = tipo;
        this.descricao = descricao;
        this.valor = valor;
        this.data = data;
    }
    
    // Construtor para recuperação do banco
    public Custo(int id, String tipo, String descricao, double valor, LocalDate data) {
        this.id = id;
        this.tipo = tipo;
        this.descricao = descricao;
        this.valor = valor;
        this.data = data;
    }
    
    // Getters e Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    
    public double getValor() { return valor; }
    public void setValor(double valor) { this.valor = valor; }
    
    public LocalDate getData() { return data; }
    public void setData(LocalDate data) { this.data = data; }
}
