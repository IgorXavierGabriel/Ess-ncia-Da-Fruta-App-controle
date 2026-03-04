package com.essenciadafruta.model;

/**
 * Represents a batch or item of fruit pulp produced by the company.
 */
public class Polpa {
    private String sabor;
    private double peso;
    private int quantidade;
    private double preco;

    public Polpa() {
    }

    public Polpa(String sabor, double peso, int quantidade, double preco) {
        this.sabor = sabor;
        this.peso = peso;
        this.quantidade = quantidade;
        this.preco = preco;
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

    @Override
    public String toString() {
        return "Polpa{" +
                "sabor='" + sabor + '\'' +
                ", peso=" + peso +
                ", quantidade=" + quantidade +
                ", preco=" + preco +
                '}';
    }
}
