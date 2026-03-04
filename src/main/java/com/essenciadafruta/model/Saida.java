package com.essenciadafruta.model;

import java.time.LocalDate;

/**
 * Classe que representa uma Saída (Pedido de Venda)
 */
public class Saida {
    private int id;
    private int polpaId;
    private int quantidade;
    private double preco;
    private LocalDate dataSaida;
    private String metodoPagamento; // Dinheiro, Cartão, Boleto, PIX
    private String cliente;
    private String status; // Pendente, Pago, Cancelado
    
    public Saida() {
    }
    
    public Saida(int polpaId, int quantidade, double preco, LocalDate dataSaida, String metodoPagamento, String cliente) {
        this.polpaId = polpaId;
        this.quantidade = quantidade;
        this.preco = preco;
        this.dataSaida = dataSaida;
        this.metodoPagamento = metodoPagamento;
        this.cliente = cliente;
        this.status = "Pendente";
    }
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public int getPolpaId() {
        return polpaId;
    }
    
    public void setPolpaId(int polpaId) {
        this.polpaId = polpaId;
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
    
    public LocalDate getDataSaida() {
        return dataSaida;
    }
    
    public void setDataSaida(LocalDate dataSaida) {
        this.dataSaida = dataSaida;
    }
    
    public String getMetodoPagamento() {
        return metodoPagamento;
    }
    
    public void setMetodoPagamento(String metodoPagamento) {
        this.metodoPagamento = metodoPagamento;
    }
    
    public String getCliente() {
        return cliente;
    }
    
    public void setCliente(String cliente) {
        this.cliente = cliente;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public double getValorTotal() {
        return quantidade * preco;
    }
    
    @Override
    public String toString() {
        return String.format("Saida{id=%d, cliente='%s', qtd=%d, valor=R$%.2f, metodo='%s', data=%s}", 
                id, cliente, quantidade, getValorTotal(), metodoPagamento, dataSaida);
    }
}
