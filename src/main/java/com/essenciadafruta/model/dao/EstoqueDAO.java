package com.essenciadafruta.model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

import com.essenciadafruta.model.Polpa;
import com.essenciadafruta.util.Database;

public class EstoqueDAO {

    /**
     * Lista todas as produções do dia
     */
    public List<Polpa> listarPorDia(LocalDate data) throws SQLException {
        String sql = "SELECT * FROM polpa WHERE data_producao = ? ORDER BY data_producao DESC";
        List<Polpa> lista = new ArrayList<>();

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, data.toString());
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Polpa p = construirPolpa(rs);
                lista.add(p);
            }
        }
        return lista;
    }

    /**
     * Lista todas as produções da semana (últimos 7 dias)
     */
    public List<Polpa> listarPorSemana(LocalDate dataFim) throws SQLException {
        LocalDate dataInicio = dataFim.minusDays(6);
        String sql = "SELECT * FROM polpa WHERE data_producao >= ? AND data_producao <= ? ORDER BY data_producao DESC";
        List<Polpa> lista = new ArrayList<>();

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, dataInicio.toString());
            ps.setString(2, dataFim.toString());
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Polpa p = construirPolpa(rs);
                lista.add(p);
            }
        }
        return lista;
    }

    /**
     * Lista todas as produções do mês
     */
    public List<Polpa> listarPorMes(int ano, int mes) throws SQLException {
        String sql = "SELECT * FROM polpa WHERE data_producao >= ? AND data_producao < ? ORDER BY data_producao DESC";
        List<Polpa> lista = new ArrayList<>();
        YearMonth inicio = YearMonth.of(ano, mes);

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, inicio.atDay(1).toString());
            ps.setString(2, inicio.plusMonths(1).atDay(1).toString());
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Polpa p = construirPolpa(rs);
                lista.add(p);
            }
        }
        return lista;
    }

    /**
     * Lista todas as produções do ano
     */
    public List<Polpa> listarPorAno(int ano) throws SQLException {
        String sql = "SELECT * FROM polpa WHERE data_producao >= ? AND data_producao < ? ORDER BY data_producao DESC";
        List<Polpa> lista = new ArrayList<>();

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, LocalDate.of(ano, 1, 1).toString());
            ps.setString(2, LocalDate.of(ano + 1, 1, 1).toString());
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Polpa p = construirPolpa(rs);
                lista.add(p);
            }
        }
        return lista;
    }

    /**
     * Lista todas as produções (estoque atual)
     */
    public List<Polpa> listarTodas() throws SQLException {
        String sql = "SELECT * FROM polpa ORDER BY data_producao DESC";
        List<Polpa> lista = new ArrayList<>();

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Polpa p = construirPolpa(rs);
                lista.add(p);
            }
        }
        return lista;
    }

    /**
     * Retorna a quantidade em estoque de um sabor específico
     */
    public int getQuantidadeDisponivel(int polpaId) throws SQLException {
        String sql = "SELECT quantidade FROM polpa WHERE id = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, polpaId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt("quantidade");
            }
        }
        return 0;
    }

    /**
     * Busca o ID de uma polpa baseada em sabor e unidade (peso)
     */
    public Integer getPolpaId(String sabor, String unidade) throws SQLException {
        String sql = "SELECT id FROM polpa WHERE sabor = ? AND unidade = ? LIMIT 1";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, sabor);
            ps.setString(2, unidade);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
        }
        return null;
    }

    /**
     * Reduz a quantidade do estoque (usada quando vende)
     */
    public boolean reduzirQuantidade(int polpaId, int quantidadeVendida) throws SQLException {
        try (Connection conn = Database.getConnection()) {
            return reduzirQuantidade(conn, polpaId, quantidadeVendida);
        }
    }

    /** Baixa o estoque numa conexão existente. O UPDATE condicional evita estoque negativo. */
    public boolean reduzirQuantidade(Connection conn, int polpaId, int quantidadeVendida) throws SQLException {
        String sql = "UPDATE polpa SET quantidade = quantidade - ? WHERE id = ? AND quantidade >= ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, quantidadeVendida);
            ps.setInt(2, polpaId);
            ps.setInt(3, quantidadeVendida);

            int linhasAfetadas = ps.executeUpdate();
            return linhasAfetadas > 0;
        }
    }

    /**
     * Obtém uma produção pelo ID
     */
    public Polpa obterPorId(int id) throws SQLException {
        String sql = "SELECT * FROM polpa WHERE id = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return construirPolpa(rs);
            }
        }
        return null;
    }

    /**
     * Constrói objeto Polpa a partir do ResultSet
     */
    private Polpa construirPolpa(ResultSet rs) throws SQLException {
        String unidade = rs.getString("unidade");
        String validadeStr = rs.getString("data_validade");
        LocalDate validade = null;
        if (validadeStr != null && !validadeStr.trim().isEmpty()) {
            validade = LocalDate.parse(validadeStr);
        }
        Polpa p = new Polpa(
            rs.getInt("id"),
            rs.getString("sabor"),
            rs.getDouble("peso"),
            unidade,
            rs.getInt("quantidade"),
            rs.getInt("quantidade_produzida"),
            rs.getDouble("preco"),
            LocalDate.parse(rs.getString("data_producao")),
            validade,
            rs.getString("lote")
        );
        return p;
    }
}
