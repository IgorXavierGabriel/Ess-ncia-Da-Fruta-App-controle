package com.essenciadafruta.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {
    // Sobrescrevível com -Dessencia.db.url (os testes usam H2 em memória)
    private static final String URL = System.getProperty(
            "essencia.db.url", "jdbc:h2:~/essencia_da_fruta;AUTO_SERVER=TRUE");
    private static final String USER = "sa";
    private static final String PASSWORD = "";
    private static boolean inicializado;

    /**
     * Retorna uma nova conexão a cada chamada (quem chama deve fechá-la).
     * As tabelas são criadas apenas na primeira vez.
     */
    public static synchronized Connection getConnection() throws SQLException {
        Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
        if (!inicializado) {
            try {
                criarTabelas(connection);
            } catch (SQLException e) {
                connection.close();
                throw e;
            }
            inicializado = true;
        }
        return connection;
    }

    private static void criarTabelas(Connection conn) throws SQLException {
        // Tabela POLPA
        String sqlPolpa = "CREATE TABLE IF NOT EXISTS polpa (" +
                "id INT AUTO_INCREMENT PRIMARY KEY," +
                "sabor VARCHAR(100) NOT NULL," +
                "peso DOUBLE NOT NULL," +
                "unidade VARCHAR(50) NOT NULL," +
                "quantidade INT NOT NULL," +
                "quantidade_produzida INT NOT NULL," +
                "preco DOUBLE DEFAULT 0," +
                "data_producao VARCHAR(10) NOT NULL," +
                "data_validade VARCHAR(10) NOT NULL," +
                "lote VARCHAR(100) UNIQUE NOT NULL" +
                ")";

        // Tabela CUSTO
        String sqlCusto = "CREATE TABLE IF NOT EXISTS custo (" +
                "id INT AUTO_INCREMENT PRIMARY KEY," +
                "tipo VARCHAR(100) NOT NULL," +
                "descricao VARCHAR(255) NOT NULL," +
                "valor DOUBLE NOT NULL," +
                "data VARCHAR(10) NOT NULL" +
                ")";

        // Tabela SAIDA (Vendas)
        String sqlSaida = "CREATE TABLE IF NOT EXISTS saida (" +
                "id INT AUTO_INCREMENT PRIMARY KEY," +
                "polpa_id INT NOT NULL," +
                "sabor VARCHAR(100)," +
                "peso_unitario VARCHAR(50)," +
                "quantidade INT NOT NULL," +
                "preco DOUBLE NOT NULL," +
                "whatsapp VARCHAR(20)," +
                "endereco VARCHAR(255)," +
                "data_entrega VARCHAR(10)," +
                "data_saida VARCHAR(10) NOT NULL," +
                "metodo_pagamento VARCHAR(50)," +
                "cliente VARCHAR(150) NOT NULL," +
                "status VARCHAR(50) DEFAULT 'Pendente'," +
                "FOREIGN KEY (polpa_id) REFERENCES polpa(id)" +
                ")";

        // Tabela FINANCEIRO (opcional para cálculos)
        String sqlFinanceiro = "CREATE TABLE IF NOT EXISTS financeiro (" +
                "id INT AUTO_INCREMENT PRIMARY KEY," +
                "data VARCHAR(10) NOT NULL," +
                "tipo VARCHAR(50) NOT NULL," +
                "descricao VARCHAR(255) NOT NULL," +
                "valor DOUBLE NOT NULL" +
                ")";

        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sqlPolpa);
            stmt.execute(sqlCusto);
            stmt.execute(sqlSaida);
            stmt.execute(sqlFinanceiro);

            // Migração para bancos criados antes da coluna quantidade_produzida
            stmt.execute("ALTER TABLE polpa ADD COLUMN IF NOT EXISTS quantidade_produzida INT DEFAULT 0");
            stmt.execute("UPDATE polpa SET quantidade_produzida = quantidade "
                    + "WHERE quantidade_produzida = 0 OR quantidade_produzida IS NULL");
        }
    }
}