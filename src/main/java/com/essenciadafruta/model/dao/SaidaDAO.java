package com.essenciadafruta.model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

import com.essenciadafruta.model.Saida;
import com.essenciadafruta.util.Database;

/**
 * Data Access Object para Saida (Vendas)
 */
public class SaidaDAO {
    
    public void inserir(Saida saida) throws SQLException {
        try (Connection conn = Database.getConnection()) {
            inserir(conn, saida);
        }
    }

    /** Insere usando uma conexão existente (permite participar de uma transação). */
    public void inserir(Connection conn, Saida saida) throws SQLException {
        String sql = "INSERT INTO saida (polpa_id, sabor, peso_unitario, quantidade, preco, whatsapp, endereco, data_entrega, data_saida, metodo_pagamento, cliente, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, saida.getPolpaId());
            pstmt.setString(2, saida.getSabor());
            pstmt.setString(3, saida.getPesoUnitario());
            pstmt.setInt(4, saida.getQuantidade());
            pstmt.setDouble(5, saida.getPreco());
            pstmt.setString(6, saida.getWhatsapp());
            pstmt.setString(7, saida.getEndereco());
            pstmt.setString(8, saida.getDataEntrega() == null ? null : saida.getDataEntrega().toString());
            pstmt.setString(9, saida.getDataSaida().toString());
            pstmt.setString(10, saida.getMetodoPagamento());
            pstmt.setString(11, saida.getCliente());
            pstmt.setString(12, saida.getStatus());
            pstmt.executeUpdate();
        }
    }
    
    public List<Saida> listarTodas() throws SQLException {
        List<Saida> saidas = new ArrayList<>();
        String sql = "SELECT * FROM saida ORDER BY data_saida DESC";
        try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Saida saida = new Saida(
                    rs.getInt("polpa_id"),
                    rs.getString("sabor"),
                    rs.getString("peso_unitario"),
                    rs.getInt("quantidade"),
                    rs.getDouble("preco"),
                    rs.getString("whatsapp"),
                    rs.getString("endereco"),
                    rs.getString("data_entrega") != null ? LocalDate.parse(rs.getString("data_entrega")) : null,
                    LocalDate.parse(rs.getString("data_saida")),
                    rs.getString("metodo_pagamento"),
                    rs.getString("cliente")
                );
                saida.setId(rs.getInt("id"));
                saida.setStatus(rs.getString("status"));
                saidas.add(saida);
            }
        }
        return saidas;
    }
    
    public double getTotalVendasMes() throws SQLException {
        String sql = "SELECT COALESCE(SUM(quantidade * preco), 0) as total FROM saida WHERE data_saida >= ? AND data_saida < ?";
        YearMonth mes = YearMonth.now();
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, mes.atDay(1).toString());
            ps.setString(2, mes.plusMonths(1).atDay(1).toString());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("total");
                }
            }
        }
        return 0;
    }
    
    public List<Saida> listarPorSemana(LocalDate dataFim) throws SQLException {
        LocalDate dataInicio = dataFim.minusDays(6);
        String sql = "SELECT * FROM saida WHERE data_saida >= ? AND data_saida <= ? ORDER BY data_saida DESC";
        List<Saida> lista = new ArrayList<>();
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, dataInicio.toString());
            ps.setString(2, dataFim.toString());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(construirSaida(rs));
            }
        }
        return lista;
    }
    
    public List<Saida> listarPorMes(int ano, int mes) throws SQLException {
        String sql = "SELECT * FROM saida WHERE data_saida >= ? AND data_saida < ? ORDER BY data_saida DESC";
        List<Saida> lista = new ArrayList<>();
        YearMonth inicio = YearMonth.of(ano, mes);
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, inicio.atDay(1).toString());
            ps.setString(2, inicio.plusMonths(1).atDay(1).toString());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(construirSaida(rs));
            }
        }
        return lista;
    }
    
    public List<Saida> listarPorAno(int ano) throws SQLException {
        String sql = "SELECT * FROM saida WHERE data_saida >= ? AND data_saida < ? ORDER BY data_saida DESC";
        List<Saida> lista = new ArrayList<>();
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, LocalDate.of(ano, 1, 1).toString());
            ps.setString(2, LocalDate.of(ano + 1, 1, 1).toString());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(construirSaida(rs));
            }
        }
        return lista;
    }
    
    private Saida construirSaida(ResultSet rs) throws SQLException {
        Saida saida = new Saida(
            rs.getInt("polpa_id"),
            rs.getString("sabor"),
            rs.getString("peso_unitario"),
            rs.getInt("quantidade"),
            rs.getDouble("preco"),
            rs.getString("whatsapp"),
            rs.getString("endereco"),
            rs.getString("data_entrega") != null ? LocalDate.parse(rs.getString("data_entrega")) : null,
            LocalDate.parse(rs.getString("data_saida")),
            rs.getString("metodo_pagamento"),
            rs.getString("cliente")
        );
        saida.setId(rs.getInt("id"));
        saida.setStatus(rs.getString("status"));
        return saida;
    }
}
