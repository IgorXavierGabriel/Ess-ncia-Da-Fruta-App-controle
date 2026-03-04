package com.essenciadafruta.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Classe para gerenciar conexão com banco de dados SQLite
 */
public class Database {
    private static final String URL = "jdbc:sqlite:essencia_da_fruta.db";
    private static Connection connection;

    private Database() {
    }

    public static synchronized Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(URL);
            initializeSchema();
        }
        return connection;
    }

    private static void initializeSchema() throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            // Tabela de Polpas
            stmt.execute("""
                    CREATE TABLE IF NOT EXISTS polpa (
                        id INTEGER PRIMARY KEY AUTOINCREMENT,
                        sabor TEXT NOT NULL,
                        peso REAL NOT NULL,
                        quantidade INTEGER NOT NULL,
                        preco REAL NOT NULL,
                        data_producao DATE,
                        lote TEXT
                    )
                    """);
            
            // Tabela de Estoque
            stmt.execute("""
                    CREATE TABLE IF NOT EXISTS estoque (
                        id INTEGER PRIMARY KEY AUTOINCREMENT,
                        item TEXT NOT NULL,
                        descricao TEXT,
                        quantidade REAL NOT NULL,
                        unidade TEXT NOT NULL,
                        preco REAL NOT NULL,
                        fornecedor TEXT
                    )
                    """);
            
            // Tabela de Saídas (Vendas)
            stmt.execute("""
                    CREATE TABLE IF NOT EXISTS saida (
                        id INTEGER PRIMARY KEY AUTOINCREMENT,
                        polpa_id INTEGER NOT NULL,
                        quantidade INTEGER NOT NULL,
                        preco REAL NOT NULL,
                        data_saida DATE NOT NULL,
                        metodo_pagamento TEXT NOT NULL,
                        cliente TEXT NOT NULL,
                        status TEXT DEFAULT 'Pendente',
                        FOREIGN KEY(polpa_id) REFERENCES polpa(id)
                    )
                    """);
            
            // Tabela de Financeiro
            stmt.execute("""
                    CREATE TABLE IF NOT EXISTS financeiro (
                        id INTEGER PRIMARY KEY AUTOINCREMENT,
                        tipo TEXT NOT NULL,
                        categoria TEXT NOT NULL,
                        valor REAL NOT NULL,
                        data DATE NOT NULL,
                        descricao TEXT,
                        status TEXT DEFAULT 'Pendente'
                    )
                    """);
        }
    }

    public static synchronized void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException ignored) {
            }
        }
    }
}
