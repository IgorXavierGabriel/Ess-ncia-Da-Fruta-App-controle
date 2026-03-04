package com.essenciadafruta.model;

import java.time.LocalDate;

/**
 * Classe que representa Registros Financeiros (Contas a Pagar e Receber)
 */
public class Financeiro {
    private int id;
    private String tipo; // Receita, Despesa
    private String categoria; // Vendas, Luz, Funcionários, Impostos, Gasolina, etc
    private double valor;
    private LocalDate data;
    private String descricao;
    private String status; // Pendente, Pago, Cancelado
    
    public Financeiro() {
    }
    
    public Financeiro(String tipo, String categoria, double valor, LocalDate data, String descricao) {
        this.tipo = tipo;
        this.categoria = categoria;
        this.valor = valor;
        this.data = data;
        this.descricao = descricao;
        this.status = "Pendente";
    }
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getTipo() {
        return tipo;
    }
    
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    
    public String getCategoria() {
        return categoria;
    }
    
    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
    
    public double getValor() {
        return valor;
    }
    
    public void setValor(double valor) {
        this.valor = valor;
    }
    
    public LocalDate getData() {
        return data;
    }
    
    public void setData(LocalDate data) {
        this.data = data;
    }
    
    public String getDescricao() {
        return descricao;
    }
    
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    @Override
    public String toString() {
        return String.format("Financeiro{id=%d, tipo='%s', categoria='%s', valor=R$%.2f, data=%s, status='%s'}", 
                id, tipo, categoria, valor, data, status);
    }
}
