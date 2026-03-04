package com.essenciadafruta.view;

import com.essenciadafruta.model.Polpa;
import com.formdev.flatlaf.FlatLightLaf;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.time.LocalDate;

/**
 * Janela principal do sistema Essência da Fruta
 */
public class MainWindow extends JFrame {
    
    // Cores do branding
    private static final Color VERMELHO = new Color(227, 30, 36);      // #E31E24
    private static final Color AMARELO = new Color(255, 215, 0);       // #FFD700
    private static final Color BRANCO = new Color(244, 244, 244);      // #F4F4F4
    private static final Color VERDE = new Color(76, 175, 80);         // #4CAF50
    private static final Color CINZA = new Color(51, 51, 51);          // #333333
    
    private JTabbedPane tabbedPane;
    
    public MainWindow() {
        setTitle("Essência da Fruta - Sistema de Gerenciamento");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);
        
        // Criar componentes
        createUI();
        
        setVisible(true);
    }
    
    private void createUI() {
        // Painel principal com background branco
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(BRANCO);
        
        // Header com logo
        JPanel headerPanel = createHeaderPanel();
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        
        // Painel com abas
        tabbedPane = new JTabbedPane();
        tabbedPane.setBackground(BRANCO);
        
        // Aba 1: Dashboard
        tabbedPane.addTab("📊 Dashboard", createDashboardPanel());
        
        // Aba 2: Cadastro de Produção
        tabbedPane.addTab("🏭 Produção", createProducaoPanel());
        
        // Aba 3: Registro de Vendas
        tabbedPane.addTab("💰 Vendas", createVendasPanel());
        
        // Aba 4: Estoque
        tabbedPane.addTab("📦 Estoque", createEstoquePanel());
        
        // Aba 5: Financeiro
        tabbedPane.addTab("💳 Financeiro", createFinanceiroPanel());
        
        // Aba 6: Relatórios
        tabbedPane.addTab("📈 Relatórios", createRelatoriosPanel());
        
        mainPanel.add(tabbedPane, BorderLayout.CENTER);
        
        // Footer
        JPanel footerPanel = createFooterPanel();
        mainPanel.add(footerPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    private JPanel createHeaderPanel() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(VERMELHO);
        header.setPreferredSize(new Dimension(0, 80));
        header.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        // Logo/Título
        JLabel titleLabel = new JLabel("🍓 ESSÊNCIA DA FRUTA");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(AMARELO);
        
        // Subtítulo
        JLabel subtitleLabel = new JLabel("Sistema de Gerenciamento de Produção e Vendas");
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        subtitleLabel.setForeground(BRANCO);
        
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBackground(VERMELHO);
        leftPanel.add(titleLabel, BorderLayout.NORTH);
        leftPanel.add(subtitleLabel, BorderLayout.SOUTH);
        
        header.add(leftPanel, BorderLayout.WEST);
        
        // Data atual
        JLabel dateLabel = new JLabel(LocalDate.now().toString());
        dateLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        dateLabel.setForeground(BRANCO);
        header.add(dateLabel, BorderLayout.EAST);
        
        return header;
    }
    
    private JPanel createDashboardPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BRANCO);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Cards de informações
        JPanel cardsPanel = new JPanel(new GridLayout(2, 2, 15, 15));
        cardsPanel.setBackground(BRANCO);
        
        cardsPanel.add(createCardPanel("Polpas em Estoque", "124", VERDE));
        cardsPanel.add(createCardPanel("Vendas do Mês", "R$ 5.230,00", AMARELO));
        cardsPanel.add(createCardPanel("Matéria Prima", "85 kg", VERMELHO));
        cardsPanel.add(createCardPanel("Pendências", "3", new Color(255, 193, 7)));
        
        panel.add(cardsPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createCardPanel(String titulo, String valor, Color cor) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(cor);
        card.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        card.setPreferredSize(new Dimension(200, 100));
        
        JLabel titleLabel = new JLabel(titulo);
        titleLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        titleLabel.setForeground(CINZA);
        
        JLabel valueLabel = new JLabel(valor);
        valueLabel.setFont(new Font("Arial", Font.BOLD, 24));
        valueLabel.setForeground(CINZA);
        
        card.add(titleLabel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);
        
        return card;
    }
    
    private JPanel createProducaoPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BRANCO);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Formulário de cadastro
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(BRANCO);
        formPanel.setBorder(new TitledBorder("Cadastrar Nova Produção"));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Campos
        addFormField(formPanel, "Sabor:", new JTextField(20), gbc, 0);
        addFormField(formPanel, "Peso (kg):", new JTextField(20), gbc, 1);
        addFormField(formPanel, "Quantidade:", new JTextField(20), gbc, 2);
        addFormField(formPanel, "Preço (R$):", new JTextField(20), gbc, 3);
        
        // Botão Salvar
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        JButton btnSalvar = new JButton("💾 Salvar Produção");
        btnSalvar.setBackground(VERDE);
        btnSalvar.setForeground(Color.WHITE);
        btnSalvar.setFont(new Font("Arial", Font.BOLD, 12));
        formPanel.add(btnSalvar, gbc);
        
        panel.add(formPanel, BorderLayout.NORTH);
        
        return panel;
    }
    
    private JPanel createVendasPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BRANCO);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel label = new JLabel("📝 Sistema de Registro de Vendas");
        label.setFont(new Font("Arial", Font.BOLD, 16));
        label.setForeground(VERMELHO);
        panel.add(label, BorderLayout.NORTH);
        
        // Tabela de vendas
        String[] colunas = {"ID", "Cliente", "Produto", "Quantidade", "Valor", "Método", "Status"};
        Object[][] dados = {
            {1, "João Silva", "Polpa Morango", 5, "R$ 150,00", "PIX", "Pago"},
            {2, "Maria Santos", "Polpa Maracujá", 3, "R$ 90,00", "Dinheiro", "Pendente"}
        };
        
        JTable table = new JTable(dados, colunas);
        table.setBackground(BRANCO);
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createEstoquePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BRANCO);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel label = new JLabel("📦 Controle de Estoque");
        label.setFont(new Font("Arial", Font.BOLD, 16));
        label.setForeground(VERDE);
        panel.add(label, BorderLayout.NORTH);
        
        return panel;
    }
    
    private JPanel createFinanceiroPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BRANCO);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel label = new JLabel("💳 Controle Financeiro");
        label.setFont(new Font("Arial", Font.BOLD, 16));
        label.setForeground(VERMELHO);
        panel.add(label, BorderLayout.NORTH);
        
        return panel;
    }
    
    private JPanel createRelatoriosPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BRANCO);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel label = new JLabel("📈 Relatórios e Análises");
        label.setFont(new Font("Arial", Font.BOLD, 16));
        label.setForeground(VERDE);
        panel.add(label, BorderLayout.NORTH);
        
        return panel;
    }
    
    private JPanel createFooterPanel() {
        JPanel footer = new JPanel(new BorderLayout());
        footer.setBackground(CINZA);
        footer.setPreferredSize(new Dimension(0, 40));
        footer.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        JLabel label = new JLabel("© 2026 Essência da Fruta - Todos os direitos reservados");
        label.setForeground(BRANCO);
        label.setFont(new Font("Arial", Font.PLAIN, 11));
        footer.add(label, BorderLayout.WEST);
        
        return footer;
    }
    
    private void addFormField(JPanel panel, String label, JComponent field, GridBagConstraints gbc, int row) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 1;
        JLabel lbl = new JLabel(label);
        lbl.setForeground(CINZA);
        panel.add(lbl, gbc);
        
        gbc.gridx = 1;
        panel.add(field, gbc);
    }
}
