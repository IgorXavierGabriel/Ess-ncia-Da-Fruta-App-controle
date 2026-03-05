package com.essenciadafruta.model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.essenciadafruta.model.Polpa;
import com.essenciadafruta.util.Database;

/**
 * Data Access Object para Polpa
 */
public class PolpaDAO {
    
    public void inserir(Polpa polpa) throws SQLException {
        String sql = "INSERT INTO polpa (sabor, peso, unidade, quantidade, quantidade_produzida, preco, data_producao, data_validade, lote) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, polpa.getSabor());
            pstmt.setDouble(2, polpa.getPeso());
            pstmt.setString(3, polpa.getUnidade());
            pstmt.setInt(4, polpa.getQuantidade());
            pstmt.setInt(5, polpa.getQuantidadeProduzida());
            pstmt.setDouble(6, polpa.getPreco());
            pstmt.setString(7, polpa.getDataProducao().toString());
            pstmt.setString(8, polpa.getDataValidade().toString());
            pstmt.setString(9, polpa.getLote());
            pstmt.executeUpdate();
        }
    }
    
    public List<Polpa> listarTodas() throws SQLException {
        List<Polpa> polpas = new ArrayList<>();
        String sql = "SELECT * FROM polpa ORDER BY data_producao DESC";
        try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Polpa polpa = new Polpa(
                    rs.getInt("id"),
                    rs.getString("sabor"),
                    rs.getDouble("peso"),
                    rs.getString("unidade"),
                    rs.getInt("quantidade"),
                    rs.getInt("quantidade_produzida"),
                    rs.getDouble("preco"),
                    LocalDate.parse(rs.getString("data_producao")),
                    LocalDate.parse(rs.getString("data_validade")),
                    rs.getString("lote")
                );
                polpas.add(polpa);
            }
        }
        return polpas;
    }
    
    public int contarTotal() throws SQLException {
        String sql = "SELECT COUNT(*) as total FROM polpa";
        try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt("total");
            }
        }
        return 0;
    }
    
    public int contarQuantidadeTotal() throws SQLException {
        String sql = "SELECT SUM(quantidade) as total FROM polpa";
        try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                Integer total = rs.getInt("total");
                return total != null ? total : 0;
            }
        }
        return 0;
    }
    
    public List<Polpa> listarPorSemana(LocalDate dataFim) throws SQLException {
        List<Polpa> polpas = new ArrayList<>();
        LocalDate dataInicio = dataFim.minusDays(6);
        String sql = "SELECT * FROM polpa WHERE DATE(data_producao) >= ? AND DATE(data_producao) <= ? ORDER BY data_producao DESC";
        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, dataInicio.toString());
            pstmt.setString(2, dataFim.toString());
            try (ResultSet rs = pstmt.executeQuery()) {
                polpas = construirPolpas(rs);
            }
        }
        return polpas;
    }
    
    public List<Polpa> listarPorMes(int ano, int mes) throws SQLException {
        List<Polpa> polpas = new ArrayList<>();
        String sql = "SELECT * FROM polpa WHERE CAST(STRFTIME('%Y', data_producao) AS INTEGER) = ? AND CAST(STRFTIME('%m', data_producao) AS INTEGER) = ? ORDER BY data_producao DESC";
        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, ano);
            pstmt.setInt(2, mes);
            try (ResultSet rs = pstmt.executeQuery()) {
                polpas = construirPolpas(rs);
            }
        }
        return polpas;
    }
    
    public List<Polpa> listarPorAno(int ano) throws SQLException {
        List<Polpa> polpas = new ArrayList<>();
        String sql = "SELECT * FROM polpa WHERE CAST(STRFTIME('%Y', data_producao) AS INTEGER) = ? ORDER BY data_producao DESC";
        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, ano);
            try (ResultSet rs = pstmt.executeQuery()) {
                polpas = construirPolpas(rs);
            }
        }
        return polpas;
    }
    
    private List<Polpa> construirPolpas(ResultSet rs) throws SQLException {
        List<Polpa> polpas = new ArrayList<>();
        while (rs.next()) {
            Polpa polpa = new Polpa(
                rs.getInt("id"),
                rs.getString("sabor"),
                rs.getDouble("peso"),
                rs.getString("unidade"),
                rs.getInt("quantidade"),
                rs.getInt("quantidade_produzida"),
                rs.getDouble("preco"),
                LocalDate.parse(rs.getString("data_producao")),
                LocalDate.parse(rs.getString("data_validade")),
                rs.getString("lote")
            );
            polpas.add(polpa);
        }
        return polpas;
    }
}
