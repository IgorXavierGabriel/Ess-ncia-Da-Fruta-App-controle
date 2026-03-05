package com.essenciadafruta.controller;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import com.essenciadafruta.model.Saida;
import com.essenciadafruta.model.dao.EstoqueDAO;
import com.essenciadafruta.model.dao.SaidaDAO;

/**
 * Controller para operações de Vendas
 */
public class VendasController {
    private SaidaDAO saidaDAO = new SaidaDAO();
    private EstoqueDAO estoqueDAO = new EstoqueDAO();
    
    public void registrarVenda(String cliente, String sabor, String pesoUnitario,
                                 int quantidade, double preco, String whatsapp,
                                 String endereco, String metodo, LocalDate dataEntrega) throws SQLException {
        if (cliente == null || cliente.trim().isEmpty()) {
            throw new IllegalArgumentException("Cliente não pode estar vazio");
        }
        if (sabor == null || sabor.trim().isEmpty()) {
            throw new IllegalArgumentException("Sabor não pode estar vazio");
        }
        if (pesoUnitario == null || pesoUnitario.trim().isEmpty()) {
            throw new IllegalArgumentException("Peso unitário deve ser selecionado");
        }
        if (quantidade <= 0) {
            throw new IllegalArgumentException("Quantidade deve ser maior que zero");
        }
        
        // localiza polpa no estoque
        Integer polpaId = estoqueDAO.getPolpaId(sabor, pesoUnitario);
        if (polpaId == null) {
            throw new IllegalArgumentException("Polpa não encontrada no estoque");
        }
        
        int quantidadeDisponivel = estoqueDAO.getQuantidadeDisponivel(polpaId);
        if (quantidadeDisponivel < quantidade) {
            throw new IllegalArgumentException("❌ Quantidade vendida não tem em estoque! \nDisponível: " + quantidadeDisponivel + " un de " + sabor + " (" + pesoUnitario + ")");
        }
        
        // Registra a venda
        Saida saida = new Saida(polpaId, sabor, pesoUnitario, quantidade, preco,
                                whatsapp, endereco, dataEntrega,
                                LocalDate.now(), metodo, cliente);
        saidaDAO.inserir(saida);
        
        // Reduz a quantidade do estoque
        estoqueDAO.reduzirQuantidade(polpaId, quantidade);
    }
    
    public List<Saida> listarTodas() throws SQLException {
        return saidaDAO.listarTodas();
    }
    
    public double getTotalVendasMes() throws SQLException {
        return saidaDAO.getTotalVendasMes();
    }
    
    public List<Saida> listarPorSemana(LocalDate dataFim) throws SQLException {
        return saidaDAO.listarPorSemana(dataFim);
    }
    
    public List<Saida> listarPorMes(int ano, int mes) throws SQLException {
        return saidaDAO.listarPorMes(ano, mes);
    }
    
    public List<Saida> listarPorAno(int ano) throws SQLException {
        return saidaDAO.listarPorAno(ano);
    }
}
