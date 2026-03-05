package com.essenciadafruta.model;

import java.time.LocalDate;

/**
 * Classe que representa uma Saída (Pedido de Venda)
 */
public class Saida {
    private int id;
    private int polpaId;
    private String sabor;
    private String pesoUnitario;
    private int quantidade;
    private double preco;
    private String whatsapp;
    private String endereco;
    private LocalDate dataEntrega;
    private LocalDate dataSaida;
    private String metodoPagamento; // Dinheiro, Cartão, Boleto, PIX
    private String cliente;
    private String status; // Pendente, Pago, Cancelado
    
    public Saida() {
    }
    
    /**
     * Criador simples para compatibilidade (uso interno antigo)
     */
    public Saida(int polpaId, int quantidade, double preco, LocalDate dataSaida, String metodoPagamento, String cliente) {
        this(polpaId, null, null, quantidade, preco, null, null, null, dataSaida, metodoPagamento, cliente);
    }
    
    /**
     * Construtor completo com todos os campos
     */
    public Saida(int polpaId, String sabor, String pesoUnitario, int quantidade, double preco,
                 String whatsapp, String endereco, LocalDate dataEntrega,
                 LocalDate dataSaida, String metodoPagamento, String cliente) {
        this.polpaId = polpaId;
        this.sabor = sabor;
        this.pesoUnitario = pesoUnitario;
        this.quantidade = quantidade;
        this.preco = preco;
        this.whatsapp = whatsapp;
        this.endereco = endereco;
        this.dataEntrega = dataEntrega;
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

    public String getSabor() {
        return sabor;
    }

    public void setSabor(String sabor) {
        this.sabor = sabor;
    }

    public String getPesoUnitario() {
        return pesoUnitario;
    }

    public void setPesoUnitario(String pesoUnitario) {
        this.pesoUnitario = pesoUnitario;
    }

    public String getWhatsapp() {
        return whatsapp;
    }

    public void setWhatsapp(String whatsapp) {
        this.whatsapp = whatsapp;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public LocalDate getDataEntrega() {
        return dataEntrega;
    }

    public void setDataEntrega(LocalDate dataEntrega) {
        this.dataEntrega = dataEntrega;
    }
    
    public double getValorTotal() {
        return quantidade * preco;
    }
    
    @Override
    public String toString() {
        return String.format("Saida{id=%d, cliente='%s', sabor='%s', peso='%s', qtd=%d, valor=R$%.2f, entrega=%s, metodo='%s', data=%s}", 
                id, cliente, sabor, pesoUnitario, quantidade, getValorTotal(), dataEntrega, metodoPagamento, dataSaida);
    }
}
