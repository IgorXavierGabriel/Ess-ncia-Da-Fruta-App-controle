package com.essenciadafruta.controller;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import com.essenciadafruta.model.Custo;
import com.essenciadafruta.model.dao.CustoDAO;

/**
 * Controller para operações de Custo
 */
public class CustoController {
    private CustoDAO custoDAO = new CustoDAO();
    
    public void registrarCusto(String tipo, String descricao, double valor, LocalDate data) throws SQLException {
        if (tipo == null || tipo.trim().isEmpty()) {
            throw new IllegalArgumentException("Tipo de custo não pode estar vazio");
        }
        if (descricao == null || descricao.trim().isEmpty()) {
            throw new IllegalArgumentException("Descrição não pode estar vazia");
        }
        if (valor <= 0) {
            throw new IllegalArgumentException("Valor deve ser maior que zero");
        }
        
        Custo custo = new Custo(tipo, descricao, valor, data);
        custoDAO.inserir(custo);
    }
    
    public List<Custo> listarTodos() throws SQLException {
        return custoDAO.listarTodos();
    }
    
    public List<Custo> listarPorMes(int ano, int mes) throws SQLException {
        return custoDAO.listarPorMes(ano, mes);
    }
    
    public double somarPorTipo(String tipo) throws SQLException {
        return custoDAO.somarPorTipo(tipo);
    }
    
    public double somarTodos() throws SQLException {
        return custoDAO.somarTodos();
    }
    
    public List<Custo> listarPorAno(int ano) throws SQLException {
        return custoDAO.listarPorAno(ano);
    }
    
    public List<Custo> listarUltimos3Meses() throws SQLException {
        return custoDAO.listarUltimos3Meses();
    }
    
    public List<Custo> listarMesAnterior() throws SQLException {
        return custoDAO.listarMesAnterior();
    }
}
