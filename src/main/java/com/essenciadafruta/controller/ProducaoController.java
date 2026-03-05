package com.essenciadafruta.controller;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import com.essenciadafruta.model.Polpa;
import com.essenciadafruta.model.dao.PolpaDAO;

/**
 * Controller para operações de Produção
 */
public class ProducaoController {
    private PolpaDAO polpaDAO = new PolpaDAO();
    
    /**
     * Cria um novo registro de produção. Não é mais necessário informar preço,
     * este ficará em zero por padrão; a data de produção passa a ser fornecida.
     */
    public void criarPolpa(String sabor, double peso, String unidade, int quantidade,
                            LocalDate dataProducao, LocalDate dataValidade) throws SQLException {
        if (sabor == null || sabor.trim().isEmpty()) {
            throw new IllegalArgumentException("Sabor não pode estar vazio");
        }
        if (unidade == null || unidade.trim().isEmpty()) {
            throw new IllegalArgumentException("Unidade (170g/1kg) deve ser selecionada");
        }
        if (peso <= 0 || quantidade <= 0) {
            throw new IllegalArgumentException("Peso e quantidade devem ser maiores que zero");
        }
        if (dataProducao == null || dataProducao.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Data de produção inválida");
        }
        if (dataValidade == null || dataValidade.isBefore(dataProducao)) {
            throw new IllegalArgumentException("Data de validade inválida");
        }
        
        Polpa polpa = new Polpa(sabor, peso, unidade, quantidade, 0.0,
                                dataProducao, dataValidade,
                                "LOTE-" + System.currentTimeMillis());
        
        polpaDAO.inserir(polpa);
    }
    
    public List<Polpa> listarTodas() throws SQLException {
        return polpaDAO.listarTodas();
    }
    
    public int countTotal() throws SQLException {
        return polpaDAO.contarTotal();
    }
    
    public int countQuantidadeTotal() throws SQLException {
        return polpaDAO.contarQuantidadeTotal();
    }
    
    public List<Polpa> listarPorSemana(LocalDate dataFim) throws SQLException {
        return polpaDAO.listarPorSemana(dataFim);
    }
    
    public List<Polpa> listarPorMes(int ano, int mes) throws SQLException {
        return polpaDAO.listarPorMes(ano, mes);
    }
    
    public List<Polpa> listarPorAno(int ano) throws SQLException {
        return polpaDAO.listarPorAno(ano);
    }
}
