package com.essenciadafruta.controller;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import com.essenciadafruta.model.Polpa;
import com.essenciadafruta.model.dao.EstoqueDAO;

public class EstoqueController {
    private EstoqueDAO estoqueDAO;

    public EstoqueController() {
        this.estoqueDAO = new EstoqueDAO();
    }

    /**
     * Lista todas as produções do dia
     */
    public List<Polpa> listarPorDia(LocalDate data) throws SQLException {
        return estoqueDAO.listarPorDia(data);
    }

    /**
     * Lista todas as produções da semana (últimos 7 dias)
     */
    public List<Polpa> listarPorSemana(LocalDate dataFim) throws SQLException {
        return estoqueDAO.listarPorSemana(dataFim);
    }

    /**
     * Lista todas as produções do mês
     */
    public List<Polpa> listarPorMes(int ano, int mes) throws SQLException {
        return estoqueDAO.listarPorMes(ano, mes);
    }

    /**
     * Lista todas as produções do ano
     */
    public List<Polpa> listarPorAno(int ano) throws SQLException {
        return estoqueDAO.listarPorAno(ano);
    }

    /**
     * Lista todas as produções (estoque
 atual completo)
     */
    public List<Polpa> listarTodas() throws SQLException {
        return estoqueDAO.listarTodas();
    }

    /**
     * Retorna a quantidade disponível de um produto
     */
    public int getQuantidadeDisponivel(int polpaId) throws SQLException {
        return estoqueDAO.getQuantidadeDisponivel(polpaId);
    }

    public Integer buscarPolpaId(String sabor, String unidade) throws SQLException {
        return estoqueDAO.getPolpaId(sabor, unidade);
    }

    /**
     * Reduz a quantidade em estoque (usada quando registra venda)
     */
    public boolean reduzirQuantidade(int polpaId, int quantidadeVendida) throws SQLException {
        return estoqueDAO.reduzirQuantidade(polpaId, quantidadeVendida);
    }

    /**
     * Obtém detalhes de uma produção pelo ID
     */
    public Polpa obterPorId(int id) throws SQLException {
        return estoqueDAO.obterPorId(id);
    }
}
