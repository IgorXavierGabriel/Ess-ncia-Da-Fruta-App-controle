package com.essenciadafruta.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Helper class for opening a connection to a local SQLite database.
 * The database file is stored in the user's home directory under
 * "essencia_da_fruta.db" for simplicity.
 * Tables are created on demand when the connection is first obtained.
 */
public class Database {
    private static final String URL = "jdbc:sqlite:essencia_da_fruta.db";
    private static Connection connection;

    private Database() {
        // prevent instantiation
    }

    /**
     * Returns a singleton Connection instance.  The first call will
     * also ensure that the schema exists by creating necessary tables.
     */
    public static synchronized Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(URL);
            initializeSchema();
        }
        return connection;
    }

    private static void initializeSchema() throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            // example table for Polpa; other tables should be added later
            stmt.execute("""
                    CREATE TABLE IF NOT EXISTS polpa (
                        id INTEGER PRIMARY KEY AUTOINCREMENT,
                        sabor TEXT NOT NULL,
                        peso REAL,
                        quantidade INTEGER,
                        preco REAL
                    );
                    """);
            // additional tables: estoque, saida, financeiro, etc.
        }
    }

    /**
     * Close the global connection when the application shuts down.
     */
    public static synchronized void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException ignored) {
            }
        }
    }
}
