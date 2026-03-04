package com.essenciadafruta;

import com.essenciadafruta.util.Database;
import com.essenciadafruta.view.MainWindow;
import com.formdev.flatlaf.FlatLightLaf;
import javax.swing.*;

/**
 * Classe principal do sistema Essência da Fruta
 */
public class App {
    
    public static void main(String[] args) {
        // Configurar FlatLaf como Look and Feel
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (UnsupportedLookAndFeelException ex) {
            System.err.println("Erro ao setar FlatLaf: " + ex);
        }
        
        // Inicializar banco de dados
        try {
            Database.getConnection();
            System.out.println("✓ Banco de dados inicializado com sucesso");
        } catch (Exception e) {
            System.err.println("✗ Erro ao inicializar banco de dados: " + e.getMessage());
            e.printStackTrace();
        }
        
        // Criar e mostrar a janela principal
        SwingUtilities.invokeLater(() -> {
            MainWindow mainWindow = new MainWindow();
            mainWindow.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
            mainWindow.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosing(java.awt.event.WindowEvent event) {
                    // Fechar banco de dados antes de sair
                    Database.closeConnection();
                    System.exit(0);
                }
            });
        });
    }
}
