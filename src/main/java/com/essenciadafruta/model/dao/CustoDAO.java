package com.essenciadafruta.model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

import com.essenciadafruta.model.Custo;
import com.essenciadafruta.util.Database;

/**
 * DAO para gerenciar custos no banco de dados
 */
public class CustoDAO {
    
    /**
     * Insere um novo custo no banco
     */
    public void inserir(Custo custo) throws SQLException {
        String sql = "INSERT INTO custo (tipo, descricao, valor, data) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, custo.getTipo());
            ps.setString(2, custo.getDescricao());
            ps.setDouble(3, custo.getValor());
            ps.setString(4, custo.getData().toString());
            
            ps.executeUpdate();
        }
    }
    
    /**
     * Lista todos os custos
     */
    public List<Custo> listarTodos() throws SQLException {
        String sql = "SELECT * FROM custo ORDER BY data DESC";
        List<Custo> lista = new ArrayList<>();
        
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Custo c = new Custo(
                    rs.getInt("id"),
                    rs.getString("tipo"),
                    rs.getString("descricao"),
                    rs.getDouble("valor"),
                    LocalDate.parse(rs.getString("data"))
                );
                lista.add(c);
            }
        }
        return lista;
    }
    
    /**
     * Lista custos por mês
     */
    public List<Custo> listarPorMes(int ano, int mes) throws SQLException {
        String sql = "SELECT * FROM custo WHERE data >= ? AND data < ? ORDER BY data DESC";
        List<Custo> lista = new ArrayList<>();
        YearMonth inicio = YearMonth.of(ano, mes);
        
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, inicio.atDay(1).toString());
            ps.setString(2, inicio.plusMonths(1).atDay(1).toString());
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                Custo c = new Custo(
                    rs.getInt("id"),
                    rs.getString("tipo"),
                    rs.getString("descricao"),
                    rs.getDouble("valor"),
                    LocalDate.parse(rs.getString("data"))
                );
                lista.add(c);
            }
        }
        return lista;
    }
    
    /**
     * Soma custos por tipo
     */
    public double somarPorTipo(String tipo) throws SQLException {
        String sql = "SELECT SUM(valor) as total FROM custo WHERE tipo = ?";
        
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, tipo);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return rs.getDouble("total");
            }
        }
        return 0;
    }
    
    /**
     * Retorna soma total de todos os custos
     */
    public double somarTodos() throws SQLException {
        String sql = "SELECT SUM(valor) as total FROM custo";
        
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getDouble("total");
            }
        }
        return 0;
    }
    
    /**
     * Lista custos por ano
     */
    public List<Custo> listarPorAno(int ano) throws SQLException {
        String sql = "SELECT * FROM custo WHERE data >= ? AND data < ? ORDER BY data DESC";
        List<Custo> lista = new ArrayList<>();
        
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, LocalDate.of(ano, 1, 1).toString());
            ps.setString(2, LocalDate.of(ano + 1, 1, 1).toString());
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                Custo c = new Custo(
                    rs.getInt("id"),
                    rs.getString("tipo"),
                    rs.getString("descricao"),
                    rs.getDouble("valor"),
                    LocalDate.parse(rs.getString("data"))
                );
                lista.add(c);
            }
        }
        return lista;
    }
    
    /**
     * Lista custos dos últimos 3 meses
     */
    public List<Custo> listarUltimos3Meses() throws SQLException {
        LocalDate dataFim = LocalDate.now();
        LocalDate dataInicio = dataFim.minusMonths(2);
        
        String sql = "SELECT * FROM custo WHERE data >= ? AND data <= ? ORDER BY data DESC";
        List<Custo> lista = new ArrayList<>();
        
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, dataInicio.toString());
            ps.setString(2, dataFim.toString());
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                Custo c = new Custo(
                    rs.getInt("id"),
                    rs.getString("tipo"),
                    rs.getString("descricao"),
                    rs.getDouble("valor"),
                    LocalDate.parse(rs.getString("data"))
                );
                lista.add(c);
            }
        }
        return lista;
    }
    
    /**
     * Lista custos do mês anterior
     */
    public List<Custo> listarMesAnterior() throws SQLException {
        YearMonth mesAnterior = YearMonth.now().minusMonths(1);
        return listarPorMes(mesAnterior.getYear(), mesAnterior.getMonthValue());
    }
}
