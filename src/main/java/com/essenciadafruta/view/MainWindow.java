package com.essenciadafruta.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.io.File;
import java.time.LocalDate;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

import com.essenciadafruta.controller.CustoController;
import com.essenciadafruta.controller.EstoqueController;
import com.essenciadafruta.controller.ProducaoController;
import com.essenciadafruta.controller.VendasController;
import com.essenciadafruta.model.Custo;
import com.essenciadafruta.model.Polpa;
import com.essenciadafruta.model.Saida;

public class MainWindow extends JFrame {
    private JTabbedPane tabbedPane;
    private ProducaoController producaoController;
    private EstoqueController estoqueController;
    private CustoController custoController;
    private VendasController vendasController;
    
    // Labels do Dashboard
    private JLabel lblEstoqueValor;
    private JLabel lblCustosValor;
    private JLabel lblVendasValor;
    private JLabel lblLucroValor;
    
    // Imagens
    private Image imagemFundo;
    private ImageIcon logo;
    
    public MainWindow() {
        setTitle("Essência da Fruta - Sistema");
        setSize(1400, 950);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        
        // Carregar imagens
        carregarImagens();
        
        // Definir ícone da janela
        if (logo != null) {
            setIconImage(logo.getImage());
        }
        
        // Inicializar controllers
        this.producaoController = new ProducaoController();
        this.estoqueController = new EstoqueController();
        this.custoController = new CustoController();
        this.vendasController = new VendasController();
        
        // Criar painel principal com abas
        tabbedPane = new JTabbedPane();
        tabbedPane.setBackground(Color.WHITE);
        
        // Adicionar abas
        tabbedPane.addTab("Dashboard", criarPainelDashboard());
        tabbedPane.addTab("Produção", criarPainelProducao());
        tabbedPane.addTab("Estoque", criarPainelEstoque());
        tabbedPane.addTab("Custos", criarPainelCustos());
        tabbedPane.addTab("Vendas", criarPainelVendas());
        tabbedPane.addTab("Controles", criarPainelControles());
        
        // Adicionar ao frame
        add(tabbedPane, BorderLayout.CENTER);
    }
    
    private void carregarImagens() {
        try {
            // Carregar imagem de fundo
            imagemFundo = new ImageIcon(getClass().getResource("/com/essenciadafruta/image/fundo.png")).getImage();
            // Carregar logo
            logo = new ImageIcon(getClass().getResource("/com/essenciadafruta/image/logo.png"));
        } catch (Exception e) {
            System.err.println("Erro ao carregar imagens: " + e.getMessage());
        }
    }
    
    private JPanel criarPainelComFundo() {
        return new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (imagemFundo != null) {
                    g.drawImage(imagemFundo, 0, 0, getWidth(), getHeight(), this);
                }
            }
        };
    }
    
    private JPanel criarCabecalhoComLogo(String titulo) {
        JPanel cabecalho = new JPanel(new BorderLayout());
        cabecalho.setOpaque(false);
        cabecalho.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        if (logo != null) {
            // Redimensionar logo para 80x80
            Image logoRedimensionada = logo.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
            JLabel lblLogo = new JLabel(new ImageIcon(logoRedimensionada));
            cabecalho.add(lblLogo, BorderLayout.WEST);
        }
        
        return cabecalho;
    }
    
    private JPanel criarPainelDashboard() {
        JPanel painel = criarPainelComFundo();
        painel.setLayout(new BorderLayout());
        
        // Cabeçalho com logo e botão atualizar
        JPanel cabecalho = criarCabecalhoComLogo("📊 Dashboard");
        JButton btnAtualizar = new JButton("🔄 Atualizar");
        btnAtualizar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnAtualizar.setBackground(new Color(0, 123, 255));
        btnAtualizar.setForeground(Color.WHITE);
        btnAtualizar.setFocusPainted(false);
        btnAtualizar.setBorderPainted(false);
        btnAtualizar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnAtualizar.setPreferredSize(new Dimension(140, 40));
        btnAtualizar.addActionListener(e -> atualizarDashboard());
        cabecalho.add(btnAtualizar, BorderLayout.EAST);
        painel.add(cabecalho, BorderLayout.NORTH);
        
        // Painel central com cards (2x2)
        JPanel central = new JPanel(new GridLayout(2, 2, 20, 20));
        central.setOpaque(false);
        central.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        
        // Cards dos 4 indicadores
        central.add(criarCartaoModerno("Polpas em Estoque", "0 un", "📦", new Color(16, 185, 129), true));
        central.add(criarCartaoModerno("Custos (Mês)", "R$ 0,00", "💰", new Color(239, 68, 68), false));
        central.add(criarCartaoModerno("Vendas (Mês)", "R$ 0,00", "💵", new Color(59, 130, 246), false));
        central.add(criarCartaoModerno("Lucro (Mês)", "R$ 0,00", "📈", new Color(245, 158, 11), false));
        
        painel.add(central, BorderLayout.CENTER);
        
        // Atualizar dados ao carregar
        atualizarDashboard();
        
        return painel;
    }
    
    private JPanel criarCartaoModerno(String titulo, String valorInicial, String icone, Color cor, boolean isEstoque) {
        JPanel cartao = new JPanel(new BorderLayout());
        cartao.setBackground(Color.WHITE);
        cartao.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 230), 1),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        
        JLabel lblIcone = new JLabel(icone);
        lblIcone.setFont(new Font("Segoe UI", Font.PLAIN, 36));
        lblIcone.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        
        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblTitulo.setForeground(new Color(108, 117, 125));
        
        JLabel lblValor = new JLabel(valorInicial);
        lblValor.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblValor.setForeground(new Color(33, 37, 41));
        
        // Guardar referência do label para atualização
        if (isEstoque) {
            lblEstoqueValor = lblValor;
        } else if (titulo.contains("Custos")) {
            lblCustosValor = lblValor;
        } else if (titulo.contains("Vendas")) {
            lblVendasValor = lblValor;
        } else if (titulo.contains("Lucro")) {
            lblLucroValor = lblValor;
        }
        
        JPanel topo = new JPanel(new BorderLayout());
        topo.setBackground(Color.WHITE);
        topo.add(lblIcone, BorderLayout.WEST);
        
        JPanel textos = new JPanel();
        textos.setLayout(new BoxLayout(textos, BoxLayout.Y_AXIS));
        textos.setBackground(Color.WHITE);
        textos.add(lblTitulo);
        textos.add(lblValor);
        
        cartao.add(topo, BorderLayout.NORTH);
        cartao.add(textos, BorderLayout.CENTER);
        
        return cartao;
    }
    
    private void atualizarDashboard() {
        try {
            // Calcular quantidade total em estoque
            List<Polpa> estoque = estoqueController.listarTodas();
            int totalUnidades = estoque.stream()
                .filter(p -> p.getQuantidade() > 0)
                .mapToInt(Polpa::getQuantidade)
                .sum();
            lblEstoqueValor.setText(totalUnidades + " un");
            
            // Obter mês e ano atuais
            LocalDate hoje = LocalDate.now();
            int mesAtual = hoje.getMonthValue();
            int anoAtual = hoje.getYear();
            
            // Calcular custos do mês
            List<Custo> custos = custoController.listarTodos();
            double custosMes = custos.stream()
                .filter(c -> {
                    LocalDate dataCusto = c.getData();
                    return dataCusto.getMonthValue() == mesAtual && dataCusto.getYear() == anoAtual;
                })
                .mapToDouble(Custo::getValor)
                .sum();
            lblCustosValor.setText(String.format("R$ %.2f", custosMes));
            
            // Calcular vendas do mês
            List<Saida> vendas = vendasController.listarTodas();
            double vendasMes = vendas.stream()
                .filter(v -> {
                    LocalDate dataVenda = v.getDataSaida();
                    return dataVenda.getMonthValue() == mesAtual && dataVenda.getYear() == anoAtual;
                })
                .mapToDouble(Saida::getValorTotal)
                .sum();
            lblVendasValor.setText(String.format("R$ %.2f", vendasMes));
            
            // Calcular lucro do mês
            double lucroMes = vendasMes - custosMes;
            lblLucroValor.setText(String.format("R$ %.2f", lucroMes));
            
            // Atualizar cor do lucro
            if (lucroMes >= 0) {
                lblLucroValor.setForeground(new Color(16, 185, 129));
            } else {
                lblLucroValor.setForeground(new Color(239, 68, 68));
            }
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, 
                "Erro ao atualizar dashboard: " + ex.getMessage(), 
                "Erro", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private JPanel criarPainelProducao() {
        JPanel painel = criarPainelComFundo();
        painel.setLayout(new BorderLayout());
        
        // Adicionar logo no topo
        painel.add(criarCabecalhoComLogo("Produção"), BorderLayout.NORTH);
        
        // Painel central para centralizar o formulário
        JPanel painelCentral = new JPanel();
        painelCentral.setLayout(new GridBagLayout());
        painelCentral.setOpaque(false);
        
        // Formulário
        JPanel formulario = new JPanel();
        formulario.setLayout(new GridBagLayout());
        formulario.setBackground(new Color(245, 245, 245));
        formulario.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createRaisedBevelBorder(),
            BorderFactory.createEmptyBorder(30, 30, 30, 30)
        ));
        formulario.setMaximumSize(new Dimension(580, 600));
        formulario.setPreferredSize(new Dimension(580, 600));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(12, 0, 12, 0);
        gbc.weightx = 1.0;
        
        int row = 0;
        
        // Título
        JLabel titulo = new JLabel("Nova Produção");
        titulo.setFont(new Font("Arial", Font.BOLD, 20));
        titulo.setForeground(new Color(76, 175, 80));
        gbc.gridx = 0;
        gbc.gridy = row++;
        gbc.insets = new Insets(0, 0, 25, 0);
        formulario.add(titulo, gbc);
        gbc.insets = new Insets(12, 0, 12, 0);
        
        // Sabor
        JLabel lblSabor = new JLabel("Sabor");
        lblSabor.setFont(new Font("Arial", Font.BOLD, 12));
        lblSabor.setForeground(new Color(60, 60, 60));
        gbc.gridx = 0;
        gbc.gridy = row++;
        formulario.add(lblSabor, gbc);
        
        String[] sabores = {"Abacaxi", "Abacaxi c/ Hortelã", "Acerola", "Cajá", "Caju", "Uva", 
                           "Goiaba", "Manga", "Maracujá", "Morango", "Tamarindo", "Umbu", "Coquinho", "Limão"};
        JComboBox<String> sabor = new JComboBox<>(sabores);
        sabor.setFont(new Font("Arial", 0, 12));
        sabor.setPreferredSize(new Dimension(520, 35));
        sabor.setBackground(Color.WHITE);
        sabor.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
        gbc.gridx = 0;
        gbc.gridy = row++;
        gbc.insets = new Insets(5, 0, 12, 0);
        formulario.add(sabor, gbc);
        gbc.insets = new Insets(12, 0, 12, 0);
        
        // Número de Lote
        JLabel lblLote = new JLabel("Número de Lote");
        lblLote.setFont(new Font("Arial", Font.BOLD, 12));
        lblLote.setForeground(new Color(60, 60, 60));
        gbc.gridx = 0;
        gbc.gridy = row++;
        formulario.add(lblLote, gbc);
        
        JTextField lote = new JTextField();
        lote.setFont(new Font("Arial", 0, 12));
        lote.setPreferredSize(new Dimension(520, 35));
        lote.setBackground(Color.WHITE);
        lote.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
        gbc.gridx = 0;
        gbc.gridy = row++;
        gbc.insets = new Insets(5, 0, 12, 0);
        formulario.add(lote, gbc);
        gbc.insets = new Insets(12, 0, 12, 0);
        
        // Unidade
        JLabel lblUnidade = new JLabel("Unidade");
        lblUnidade.setFont(new Font("Arial", Font.BOLD, 12));
        lblUnidade.setForeground(new Color(60, 60, 60));
        gbc.gridx = 0;
        gbc.gridy = row++;
        formulario.add(lblUnidade, gbc);
        
        JComboBox<String> unidade = new JComboBox<>(new String[]{"170g", "1kg"});
        unidade.setFont(new Font("Arial", 0, 12));
        unidade.setPreferredSize(new Dimension(520, 35));
        unidade.setBackground(Color.WHITE);
        unidade.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
        gbc.gridx = 0;
        gbc.gridy = row++;
        gbc.insets = new Insets(5, 0, 12, 0);
        formulario.add(unidade, gbc);
        gbc.insets = new Insets(12, 0, 12, 0);
        
        // Quantidade
        JLabel lblQuantidade = new JLabel("Quantidade");
        lblQuantidade.setFont(new Font("Arial", Font.BOLD, 12));
        lblQuantidade.setForeground(new Color(60, 60, 60));
        gbc.gridx = 0;
        gbc.gridy = row++;
        formulario.add(lblQuantidade, gbc);
        
        JTextField quantidade = new JTextField();
        quantidade.setFont(new Font("Arial", 0, 12));
        quantidade.setPreferredSize(new Dimension(520, 35));
        quantidade.setBackground(Color.WHITE);
        quantidade.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
        gbc.gridx = 0;
        gbc.gridy = row++;
        gbc.insets = new Insets(5, 0, 12, 0);
        formulario.add(quantidade, gbc);
        gbc.insets = new Insets(12, 0, 12, 0);
        
        // Data de Produção
        JLabel lblDataProducao = new JLabel("Data de Produção");
        lblDataProducao.setFont(new Font("Arial", Font.BOLD, 12));
        lblDataProducao.setForeground(new Color(60, 60, 60));
        gbc.gridx = 0;
        gbc.gridy = row++;
        formulario.add(lblDataProducao, gbc);
        
        JTextField dataProducao = new JTextField();
        dataProducao.setFont(new Font("Arial", 0, 12));
        dataProducao.setPreferredSize(new Dimension(520, 35));
        dataProducao.setBackground(Color.WHITE);
        dataProducao.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
        dataProducao.setText(LocalDate.now().toString());
        gbc.gridx = 0;
        gbc.gridy = row++;
        gbc.insets = new Insets(5, 0, 12, 0);
        formulario.add(dataProducao, gbc);
        gbc.insets = new Insets(12, 0, 12, 0);
        
        // Data de Validade
        JLabel lblDataValidade = new JLabel("Data de Validade");
        lblDataValidade.setFont(new Font("Arial", Font.BOLD, 12));
        lblDataValidade.setForeground(new Color(60, 60, 60));
        gbc.gridx = 0;
        gbc.gridy = row++;
        formulario.add(lblDataValidade, gbc);
        
        JTextField dataValidade = new JTextField();
        dataValidade.setFont(new Font("Arial", 0, 12));
        dataValidade.setPreferredSize(new Dimension(520, 35));
        dataValidade.setBackground(Color.WHITE);
        dataValidade.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
        dataValidade.setText(LocalDate.now().plusDays(30).toString());
        gbc.gridx = 0;
        gbc.gridy = row++;
        gbc.insets = new Insets(5, 0, 25, 0);
        formulario.add(dataValidade, gbc);
        
        // Botão
        JButton btnAdicionar = new JButton("Adicionar Produção");
        btnAdicionar.setBackground(new Color(76, 175, 80));
        btnAdicionar.setForeground(Color.WHITE);
        btnAdicionar.setFont(new Font("Arial", Font.BOLD, 14));
        btnAdicionar.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        btnAdicionar.setFocusPainted(false);
        btnAdicionar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        btnAdicionar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnAdicionar.setBackground(new Color(68, 157, 72));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnAdicionar.setBackground(new Color(76, 175, 80));
            }
        });
        
        gbc.gridx = 0;
        gbc.gridy = row++;
        gbc.insets = new Insets(0, 0, 0, 0);
        formulario.add(btnAdicionar, gbc);
        
        btnAdicionar.addActionListener(e -> {
            try {
                producaoController.criarPolpa(
                    sabor.getSelectedItem().toString(),
                    1.0,
                    unidade.getSelectedItem().toString(),
                    Integer.parseInt(quantidade.getText()),
                    LocalDate.parse(dataProducao.getText()),
                    LocalDate.parse(dataValidade.getText())
                );
                JOptionPane.showMessageDialog(null, 
                    "✓ Produção registrada com sucesso!\n\nSabor: " + sabor.getSelectedItem() + 
                    "\nQuantidade: " + quantidade.getText() + 
                    "\nUnidade: " + unidade.getSelectedItem() +
                    "\nLote: " + lote.getText(),
                    "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                lote.setText("");
                quantidade.setText("");
                dataProducao.setText(LocalDate.now().toString());
                dataValidade.setText(LocalDate.now().plusDays(30).toString());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Erro: Quantidade deve ser um número!", "Erro", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Erro: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        // Centralizar o formulário na tela
        GridBagConstraints gbcMain = new GridBagConstraints();
        gbcMain.gridx = 0;
        gbcMain.gridy = 0;
        gbcMain.anchor = GridBagConstraints.CENTER;
        painelCentral.add(formulario, gbcMain);
        
        // Adicionar barra de rolagem
        JScrollPane scrollPane = new JScrollPane(painelCentral);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        painel.add(scrollPane, BorderLayout.CENTER);
        
        JLabel infoLabel = new JLabel("As produções são automaticamente adicionadas ao estoque");
        infoLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        infoLabel.setForeground(new Color(100, 100, 100));
        infoLabel.setHorizontalAlignment(JLabel.CENTER);
        infoLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        painel.add(infoLabel, BorderLayout.SOUTH);
        
        return painel;
    }
    
    private JPanel criarPainelEstoque() {
        JPanel painel = criarPainelComFundo();
        painel.setLayout(new BorderLayout());
        
        // Adicionar logo no topo
        painel.add(criarCabecalhoComLogo("Estoque de Polpas"), BorderLayout.NORTH);
        
        // Painel superior com total e botão
        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.setOpaque(false);
        panelSuperior.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel totalLabel = new JLabel();
        totalLabel.setFont(new Font("Arial", Font.BOLD, 16));
        totalLabel.setForeground(new Color(33, 150, 243));
        panelSuperior.add(totalLabel, BorderLayout.WEST);
        
        JButton btnAtualizar = new JButton("🔄 Atualizar");
        btnAtualizar.setBackground(new Color(33, 150, 243));
        btnAtualizar.setForeground(Color.WHITE);
        btnAtualizar.setFont(new Font("Arial", Font.BOLD, 12));
        btnAtualizar.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        btnAtualizar.setFocusPainted(false);
        btnAtualizar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        btnAtualizar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnAtualizar.setBackground(new Color(25, 130, 209));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnAtualizar.setBackground(new Color(33, 150, 243));
            }
        });
        
        panelSuperior.add(btnAtualizar, BorderLayout.EAST);
        
        JPanel central = new JPanel(new BorderLayout());
        central.setOpaque(false);
        central.add(panelSuperior, BorderLayout.NORTH);
        
        // Painel central com dois quadrados verticais
        JPanel containerEstoque = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 20));
        containerEstoque.setOpaque(false);
        containerEstoque.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        
        // Quadrado 170g
        JPanel quadrado170 = criarQuadroEstoque("170g");
        containerEstoque.add(quadrado170);
        
        // Quadrado 1kg
        JPanel quadrado1kg = criarQuadroEstoque("1kg");
        containerEstoque.add(quadrado1kg);
        
        central.add(containerEstoque, BorderLayout.CENTER);
        
        // Adicionar barra de rolagem
        JScrollPane scrollPane = new JScrollPane(central);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        painel.add(scrollPane, BorderLayout.CENTER);
        
        // Atualizar dados
        atualizarEstoque(totalLabel, quadrado170, quadrado1kg);
        
        // Ação do botão
        btnAtualizar.addActionListener(e -> {
            atualizarEstoque(totalLabel, quadrado170, quadrado1kg);
            JOptionPane.showMessageDialog(null, "✓ Estoque atualizado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        });
        
        return painel;
    }
    
    private JPanel criarQuadroEstoque(String unidade) {
        JPanel quadro = new JPanel(new BorderLayout());
        quadro.setBackground(new Color(245, 245, 245));
        quadro.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 2));
        quadro.setPreferredSize(new Dimension(350, 650));
        
        // Titulo do quadro
        JLabel lblUnidade = new JLabel(unidade);
        lblUnidade.setFont(new Font("Arial", Font.BOLD, 16));
        lblUnidade.setForeground(Color.WHITE);
        lblUnidade.setOpaque(true);
        lblUnidade.setBackground(new Color(76, 175, 80));
        lblUnidade.setHorizontalAlignment(JLabel.CENTER);
        lblUnidade.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        quadro.add(lblUnidade, BorderLayout.NORTH);
        
        // Painel com lista de sabores
        JPanel listaPainel = new JPanel();
        listaPainel.setLayout(new BoxLayout(listaPainel, BoxLayout.Y_AXIS));
        listaPainel.setBackground(Color.WHITE);
        listaPainel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        String[] sabores = {"Abacaxi", "Abacaxi c/ Hortelã", "Acerola", "Cajá", "Caju", "Uva", 
                           "Goiaba", "Manga", "Maracujá", "Morango", "Tamarindo", "Umbu", "Coquinho", "Limão"};
        
        for (String sabor : sabores) {
            JPanel itemPainel = new JPanel(new BorderLayout());
            itemPainel.setBackground(Color.WHITE);
            itemPainel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(230, 230, 230)));
            itemPainel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
            
            JLabel lblSabor = new JLabel(sabor);
            lblSabor.setFont(new Font("Arial", 0, 12));
            lblSabor.setForeground(new Color(60, 60, 60));
            itemPainel.add(lblSabor, BorderLayout.WEST);
            
            JLabel lblQuantidade = new JLabel("0");
            lblQuantidade.setFont(new Font("Arial", Font.BOLD, 12));
            lblQuantidade.setForeground(new Color(33, 150, 243));
            lblQuantidade.setName(unidade + "_" + sabor);
            itemPainel.add(lblQuantidade, BorderLayout.EAST);
            
            listaPainel.add(itemPainel);
        }
        
        JScrollPane scrollPane = new JScrollPane(listaPainel);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        quadro.add(scrollPane, BorderLayout.CENTER);
        
        return quadro;
    }
    
    private void atualizarEstoque(JLabel totalLabel, JPanel quadrado170, JPanel quadrado1kg) {
        try {
            List<Polpa> estoque = estoqueController.listarTodas();
            
            // Criar mapa com sabores e quantidades
            java.util.Map<String, java.util.Map<String, Integer>> estoqueMap = new java.util.HashMap<>();
            
            for (Polpa p : estoque) {
                String sabor = p.getSabor();
                String unidade = p.getUnidade();
                int quantidade = p.getQuantidade();
                
                if (!estoqueMap.containsKey(unidade)) {
                    estoqueMap.put(unidade, new java.util.HashMap<>());
                }
                
                estoqueMap.get(unidade).put(sabor, estoqueMap.get(unidade).getOrDefault(sabor, 0) + quantidade);
            }
            
            // Atualizar quadros
            atualizarQuadro(quadrado170, "170g", estoqueMap);
            atualizarQuadro(quadrado1kg, "1kg", estoqueMap);
            
            // Total
            int total = 0;
            for (Polpa p : estoque) {
                total += p.getQuantidade();
            }
            totalLabel.setText("Total em Estoque: " + total + " un");
            
        } catch (Exception ex) {
            System.err.println("Erro ao carregar estoque: " + ex.getMessage());
        }
    }
    
    private void atualizarQuadro(JPanel quadro, String unidade, java.util.Map<String, java.util.Map<String, Integer>> estoqueMap) {
        String[] sabores = {"Abacaxi", "Abacaxi c/ Hortelã", "Acerola", "Cajá", "Caju", "Uva", 
                           "Goiaba", "Manga", "Maracujá", "Morango", "Tamarindo", "Umbu", "Coquinho", "Limão"};
        
        java.util.Map<String, Integer> unidadeEstoque = estoqueMap.getOrDefault(unidade, new java.util.HashMap<>());
        
        for (JLabel lbl : buscaLabelsQuantidade(quadro, unidade)) {
            for (String sabor : sabores) {
                if (lbl.getName().equals(unidade + "_" + sabor)) {
                    int qtd = unidadeEstoque.getOrDefault(sabor, 0);
                    lbl.setText(String.valueOf(qtd));
                    break;
                }
            }
        }
    }
    
    private java.util.List<JLabel> buscaLabelsQuantidade(JPanel painel, String unidade) {
        java.util.List<JLabel> labels = new java.util.ArrayList<>();
        buscaLabelsRec(painel, unidade, labels);
        return labels;
    }
    
    private void buscaLabelsRec(java.awt.Component comp, String unidade, java.util.List<JLabel> labels) {
        if (comp instanceof JLabel) {
            JLabel lbl = (JLabel) comp;
            if (lbl.getName() != null && lbl.getName().startsWith(unidade + "_")) {
                labels.add(lbl);
            }
        } else if (comp instanceof java.awt.Container) {
            for (java.awt.Component c : ((java.awt.Container) comp).getComponents()) {
                buscaLabelsRec(c, unidade, labels);
            }
        }
    }
    
    private JPanel criarPainelCustos() {
        JPanel painel = criarPainelComFundo();
        painel.setLayout(new BorderLayout());
        
        // Adicionar logo no topo
        painel.add(criarCabecalhoComLogo("Custos"), BorderLayout.NORTH);
        
        // Painel central com formulário
        JPanel containerCustos = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        containerCustos.setOpaque(false);
        containerCustos.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JPanel formulario = new JPanel();
        formulario.setLayout(new GridBagLayout());
        formulario.setBackground(Color.WHITE);
        formulario.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
        formulario.setMaximumSize(new Dimension(580, 400));
        formulario.setPreferredSize(new Dimension(580, 400));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 20, 15, 20);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        
        // Título
        JLabel titulo = new JLabel("Novo Custo");
        titulo.setFont(new Font("Arial", Font.BOLD, 20));
        titulo.setForeground(new Color(244, 67, 54));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 20, 25, 20);
        formulario.add(titulo, gbc);
        
        gbc.insets = new Insets(10, 20, 10, 20);
        
        // Tipo
        JLabel lblTipo = new JLabel("Tipo:");
        lblTipo.setFont(new Font("Arial", Font.BOLD, 12));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weighty = 0;
        formulario.add(lblTipo, gbc);
        
        JComboBox<String> tipo = new JComboBox<>(new String[]{"Insumo", "Embalagem", "Transporte", "Mão de Obra", "Energia", "Imposto", "Outros"});
        tipo.setPreferredSize(new Dimension(520, 35));
        tipo.setBackground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 2;
        formulario.add(tipo, gbc);
        
        // Descrição
        JLabel lblDescricao = new JLabel("Descrição:");
        lblDescricao.setFont(new Font("Arial", Font.BOLD, 12));
        gbc.gridx = 0;
        gbc.gridy = 3;
        formulario.add(lblDescricao, gbc);
        
        JTextField descricao = new JTextField();
        descricao.setPreferredSize(new Dimension(520, 35));
        descricao.setBackground(Color.WHITE);
        descricao.setBorder(BorderFactory.createLineBorder(new Color(180, 180, 180), 1));
        gbc.gridx = 0;
        gbc.gridy = 4;
        formulario.add(descricao, gbc);
        
        // Valor
        JLabel lblValor = new JLabel("Valor (R$):");
        lblValor.setFont(new Font("Arial", Font.BOLD, 12));
        gbc.gridx = 0;
        gbc.gridy = 5;
        formulario.add(lblValor, gbc);
        
        JTextField valor = new JTextField();
        valor.setPreferredSize(new Dimension(520, 35));
        valor.setBackground(Color.WHITE);
        valor.setBorder(BorderFactory.createLineBorder(new Color(180, 180, 180), 1));
        gbc.gridx = 0;
        gbc.gridy = 6;
        formulario.add(valor, gbc);
        
        // Botão
        JButton btnAdicionar = new JButton("Adicionar Custo");
        btnAdicionar.setPreferredSize(new Dimension(200, 40));
        btnAdicionar.setBackground(new Color(244, 67, 54));
        btnAdicionar.setForeground(Color.WHITE);
        btnAdicionar.setFont(new Font("Arial", Font.BOLD, 14));
        btnAdicionar.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        btnAdicionar.setFocusPainted(false);
        btnAdicionar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        btnAdicionar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnAdicionar.setBackground(new Color(229, 57, 53));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnAdicionar.setBackground(new Color(244, 67, 54));
            }
        });
        
        btnAdicionar.addActionListener(e -> {
            try {
                if (descricao.getText().trim().isEmpty() || valor.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Por favor, preencha todos os campos!", "Aviso", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                
                custoController.registrarCusto(
                    tipo.getSelectedItem().toString(),
                    descricao.getText(),
                    Double.parseDouble(valor.getText()),
                    LocalDate.now()
                );
                
                JOptionPane.showMessageDialog(null, "✓ Custo registrado com sucesso!\nTipo: " + tipo.getSelectedItem() + "\nValor: R$ " + valor.getText(), "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                descricao.setText("");
                valor.setText("");
                tipo.setSelectedIndex(0);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Erro: Valor deve ser um número!", "Erro", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Erro: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.insets = new Insets(30, 20, 20, 20);
        formulario.add(btnAdicionar, gbc);
        
        containerCustos.add(formulario);
        painel.add(containerCustos, BorderLayout.CENTER);
        
        return painel;
    }
    
    private JPanel criarPainelVendas() {
        JPanel painel = criarPainelComFundo();
        painel.setLayout(new BorderLayout());
        
        // Adicionar logo no topo
        painel.add(criarCabecalhoComLogo("Vendas"), BorderLayout.NORTH);
        
        // Painel central
        JPanel central = new JPanel();
        central.setLayout(new BorderLayout());
        central.setOpaque(false);
        central.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JPanel containerVendas = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        containerVendas.setOpaque(false);
        
        JPanel formulario = new JPanel(new GridBagLayout());
        formulario.setBackground(Color.WHITE);
        formulario.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
        formulario.setMaximumSize(new Dimension(580, 760));
        formulario.setPreferredSize(new Dimension(580, 760));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 20, 10, 20);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        
        JLabel titulo = new JLabel("Nova Venda");
        titulo.setFont(new Font("Arial", Font.BOLD, 20));
        titulo.setForeground(new Color(33, 150, 243));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 20, 25, 20);
        formulario.add(titulo, gbc);
        
        gbc.insets = new Insets(10, 20, 10, 20);
        
        JLabel lblCliente = new JLabel("Cliente:");
        lblCliente.setFont(new Font("Arial", Font.BOLD, 12));
        gbc.gridy = 1;
        formulario.add(lblCliente, gbc);
        
        JComboBox<String> cliente = new JComboBox<>();
        cliente.setEditable(true);
        cliente.setPreferredSize(new Dimension(520, 35));
        cliente.setBackground(Color.WHITE);
        for (String nome : carregarClientesHistorico()) {
            cliente.addItem(nome);
        }
        gbc.gridy = 2;
        formulario.add(cliente, gbc);
        
        JLabel lblSabor = new JLabel("Sabor:");
        lblSabor.setFont(new Font("Arial", Font.BOLD, 12));
        gbc.gridy = 3;
        formulario.add(lblSabor, gbc);
        
        String[] sabores = {"Abacaxi", "Abacaxi c/ Hortelã", "Acerola", "Cajá", "Caju", "Uva", "Goiaba", "Manga", "Maracujá", "Morango", "Tamarindo", "Umbu", "Coquinho", "Limão"};
        JComboBox<String> sabor = new JComboBox<>(sabores);
        sabor.setPreferredSize(new Dimension(520, 35));
        sabor.setBackground(Color.WHITE);
        gbc.gridy = 4;
        formulario.add(sabor, gbc);
        
        JLabel lblUnidade = new JLabel("Unidade:");
        lblUnidade.setFont(new Font("Arial", Font.BOLD, 12));
        gbc.gridy = 5;
        formulario.add(lblUnidade, gbc);
        
        JComboBox<String> unidade = new JComboBox<>(new String[]{"170g", "1kg"});
        unidade.setPreferredSize(new Dimension(520, 35));
        unidade.setBackground(Color.WHITE);
        gbc.gridy = 6;
        formulario.add(unidade, gbc);
        
        JLabel lblQuantidade = new JLabel("Quantidade:");
        lblQuantidade.setFont(new Font("Arial", Font.BOLD, 12));
        gbc.gridy = 7;
        formulario.add(lblQuantidade, gbc);
        
        JTextField quantidade = new JTextField();
        quantidade.setPreferredSize(new Dimension(520, 35));
        quantidade.setBackground(Color.WHITE);
        quantidade.setBorder(BorderFactory.createLineBorder(new Color(180, 180, 180), 1));
        gbc.gridy = 8;
        formulario.add(quantidade, gbc);
        
        JLabel lblValor = new JLabel("Valor da Venda (R$):");
        lblValor.setFont(new Font("Arial", Font.BOLD, 12));
        gbc.gridy = 9;
        formulario.add(lblValor, gbc);
        
        JTextField valor = new JTextField();
        valor.setPreferredSize(new Dimension(520, 35));
        valor.setBackground(Color.WHITE);
        valor.setBorder(BorderFactory.createLineBorder(new Color(180, 180, 180), 1));
        gbc.gridy = 10;
        formulario.add(valor, gbc);
        
        JLabel lblWhatsapp = new JLabel("WhatsApp:");
        lblWhatsapp.setFont(new Font("Arial", Font.BOLD, 12));
        gbc.gridy = 11;
        formulario.add(lblWhatsapp, gbc);
        
        JTextField whatsapp = new JTextField();
        whatsapp.setPreferredSize(new Dimension(520, 35));
        whatsapp.setBackground(Color.WHITE);
        whatsapp.setBorder(BorderFactory.createLineBorder(new Color(180, 180, 180), 1));
        gbc.gridy = 12;
        formulario.add(whatsapp, gbc);
        
        JLabel lblEndereco = new JLabel("Endereço:");
        lblEndereco.setFont(new Font("Arial", Font.BOLD, 12));
        gbc.gridy = 13;
        formulario.add(lblEndereco, gbc);
        
        JTextField endereco = new JTextField();
        endereco.setPreferredSize(new Dimension(520, 35));
        endereco.setBackground(Color.WHITE);
        endereco.setBorder(BorderFactory.createLineBorder(new Color(180, 180, 180), 1));
        gbc.gridy = 14;
        formulario.add(endereco, gbc);
        
        JLabel lblPagamento = new JLabel("Forma de Pagamento:");
        lblPagamento.setFont(new Font("Arial", Font.BOLD, 12));
        gbc.gridy = 15;
        formulario.add(lblPagamento, gbc);
        
        JComboBox<String> pagamento = new JComboBox<>(new String[]{"PIX", "Cartão de Débito", "Cartão de Crédito", "Dinheiro", "Nota Fiscal"});
        pagamento.setPreferredSize(new Dimension(520, 35));
        pagamento.setBackground(Color.WHITE);
        gbc.gridy = 16;
        formulario.add(pagamento, gbc);
        
        JButton btnAdicionar = new JButton("Registrar Venda");
        btnAdicionar.setPreferredSize(new Dimension(200, 40));
        btnAdicionar.setBackground(new Color(33, 150, 243));
        btnAdicionar.setForeground(Color.WHITE);
        btnAdicionar.setFont(new Font("Arial", Font.BOLD, 14));
        btnAdicionar.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        btnAdicionar.setFocusPainted(false);
        btnAdicionar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        btnAdicionar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnAdicionar.setBackground(new Color(25, 130, 209));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnAdicionar.setBackground(new Color(33, 150, 243));
            }
        });
        
        btnAdicionar.addActionListener(e -> {
            try {
                String nomeCliente = "";
                if (cliente.getEditor().getItem() != null) {
                    nomeCliente = cliente.getEditor().getItem().toString().trim();
                }
                
                if (nomeCliente.isEmpty() || quantidade.getText().trim().isEmpty() || valor.getText().trim().isEmpty() || whatsapp.getText().trim().isEmpty() || endereco.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Por favor, preencha todos os campos!", "Aviso", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                
                vendasController.registrarVenda(
                    nomeCliente,
                    sabor.getSelectedItem().toString(),
                    unidade.getSelectedItem().toString(),
                    Integer.parseInt(quantidade.getText()),
                    Double.parseDouble(valor.getText()),
                    whatsapp.getText().trim(),
                    endereco.getText().trim(),
                    pagamento.getSelectedItem().toString(),
                    LocalDate.now()
                );
                
                // Gerar PDF da venda
                String caminhoPDF = com.essenciadafruta.util.VendaPDFGenerator.gerarPDF(
                    nomeCliente,
                    sabor.getSelectedItem().toString(),
                    unidade.getSelectedItem().toString(),
                    Integer.parseInt(quantidade.getText()),
                    Double.parseDouble(valor.getText()),
                    whatsapp.getText().trim(),
                    endereco.getText().trim(),
                    pagamento.getSelectedItem().toString(),
                    LocalDate.now()
                );
                
                adicionarClienteSeNovo(cliente, nomeCliente);
                
                int resposta = JOptionPane.showConfirmDialog(null, 
                    "✓ Venda registrada com sucesso!\n\nCliente: " + nomeCliente + "\nSabor: " + sabor.getSelectedItem() + "\nQuantidade: " + quantidade.getText() + " " + unidade.getSelectedItem() + 
                    "\n\nDeseja abrir o PDF gerado para enviar ao cliente?", 
                    "Sucesso", JOptionPane.YES_NO_OPTION);
                
                if (resposta == JOptionPane.YES_OPTION && caminhoPDF != null) {
                    try {
                        if (System.getProperty("os.name").toLowerCase().contains("win")) {
                            Runtime.getRuntime().exec("explorer.exe /select,\"" + caminhoPDF + "\"");
                        } else if (System.getProperty("os.name").toLowerCase().contains("mac")) {
                            Runtime.getRuntime().exec(new String[]{"open", "-R", caminhoPDF});
                        } else {
                            Runtime.getRuntime().exec(new String[]{"xdg-open", new File(caminhoPDF).getParent()});
                        }
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, "Erro ao abrir pasta: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                    }
                }
                
                quantidade.setText("");
                valor.setText("");
                whatsapp.setText("");
                endereco.setText("");
                sabor.setSelectedIndex(0);
                unidade.setSelectedIndex(0);
                pagamento.setSelectedIndex(0);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Erro: Quantidade e valor devem ser números!", "Erro", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Erro: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        gbc.gridy = 17;
        gbc.insets = new Insets(30, 20, 20, 20);
        formulario.add(btnAdicionar, gbc);
        
        containerVendas.add(formulario);
        JScrollPane scrollPane = new JScrollPane(containerVendas);
        scrollPane.setBorder(null);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        painel.add(scrollPane, BorderLayout.CENTER);
        
        return painel;
    }

    private java.util.Set<String> carregarClientesHistorico() {
        java.util.Set<String> clientes = new java.util.LinkedHashSet<>();
        try {
            for (Saida saida : vendasController.listarTodas()) {
                if (saida.getCliente() != null && !saida.getCliente().trim().isEmpty()) {
                    clientes.add(saida.getCliente().trim());
                }
            }
        } catch (Exception ex) {
            System.err.println("Erro ao carregar clientes: " + ex.getMessage());
        }
        return clientes;
    }

    private void adicionarClienteSeNovo(JComboBox<String> clienteCombo, String cliente) {
        if (cliente == null || cliente.trim().isEmpty()) {
            return;
        }

        String nome = cliente.trim();
        for (int i = 0; i < clienteCombo.getItemCount(); i++) {
            if (nome.equalsIgnoreCase(clienteCombo.getItemAt(i))) {
                return;
            }
        }
        clienteCombo.addItem(nome);
    }
    
    private JPanel criarPainelControles() {
        JPanel painel = criarPainelComFundo();
        painel.setLayout(new BorderLayout());
        
        // Adicionar logo no topo
        painel.add(criarCabecalhoComLogo("📊 Relatórios e Controles"), BorderLayout.NORTH);
        
        // Painel central com controles e visualização
        JPanel central = new JPanel(new GridBagLayout());
        central.setOpaque(false);
        central.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.BOTH;
        
        // --- COLUNA 1: Seleção de Tipo ---
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.3;
        gbc.weighty = 0;
        JPanel painelTipo = criarPainelSelecao("Tipo de Relatório", 
            new String[]{"Produção", "Estoque", "Custos", "Vendas", "Lucro"});
        central.add(painelTipo, gbc);
        
        // --- COLUNA 2: Seleção de Período ---
        gbc.gridx = 1;
        JPanel painelPeriodo = criarPainelSelecao("Período", 
            new String[]{"Diário", "Semanal", "Mensal", "Anual", "Todos"});
        central.add(painelPeriodo, gbc);
        
        // --- COLUNA 3: Seleção de Data/Mês/Ano ---
        gbc.gridx = 2;
        JPanel painelData = criarPainelData();
        central.add(painelData, gbc);
        
        // --- LINHA 2: Botões de Ação ---
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 3;
        gbc.weighty = 0;
        JPanel painelBotoes = criarPainelBotoes();
        central.add(painelBotoes, gbc);
        
        // --- LINHA 3: Área de Visualização ---
        gbc.gridy = 2;
        gbc.weighty = 1.0;
        JPanel painelVisualizacao = criarPainelVisualizacao();
        central.add(painelVisualizacao, gbc);
        
        painel.add(central, BorderLayout.CENTER);
        
        return painel;
    }
    
    // Componentes de controle
    private JComboBox<String> comboTipo;
    private JComboBox<String> comboPeriodo;
    private JSpinner spinnerDia;
    private JComboBox<Integer> comboMes;
    private JComboBox<Integer> comboAno;
    private JComboBox<Integer> comboSemana;
    private JTextArea areaResultados;
    private JLabel lblDataEspecifica;
    
    private JPanel criarPainelSelecao(String titulo, String[] opcoes) {
        JPanel painel = new JPanel();
        painel.setLayout(new BoxLayout(painel, BoxLayout.Y_AXIS));
        painel.setBackground(new Color(245, 245, 245));
        painel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        JLabel lbl = new JLabel(titulo);
        lbl.setFont(new Font("Arial", Font.BOLD, 14));
        lbl.setForeground(new Color(60, 60, 60));
        lbl.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        painel.add(lbl);
        
        painel.add(javax.swing.Box.createVerticalStrut(10));
        
        JComboBox<String> combo = new JComboBox<>(opcoes);
        combo.setFont(new Font("Arial", Font.PLAIN, 13));
        combo.setMaximumSize(new Dimension(200, 35));
        combo.setAlignmentX(JComboBox.CENTER_ALIGNMENT);
        
        if (titulo.equals("Tipo de Relatório")) {
            comboTipo = combo;
        } else if (titulo.equals("Período")) {
            comboPeriodo = combo;
            combo.addActionListener(e -> atualizarCamposData());
        }
        
        painel.add(combo);
        
        return painel;
    }
    
    private JPanel criarPainelData() {
        JPanel painel = new JPanel();
        painel.setLayout(new BoxLayout(painel, BoxLayout.Y_AXIS));
        painel.setBackground(new Color(245, 245, 245));
        painel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        JLabel lbl = new JLabel("Data Específica");
        lbl.setFont(new Font("Arial", Font.BOLD, 14));
        lbl.setForeground(new Color(60, 60, 60));
        lbl.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        painel.add(lbl);
        
        painel.add(javax.swing.Box.createVerticalStrut(10));
        
        lblDataEspecifica = new JLabel("Selecione o período");
        lblDataEspecifica.setFont(new Font("Arial", Font.ITALIC, 12));
        lblDataEspecifica.setForeground(new Color(100, 100, 100));
        lblDataEspecifica.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        painel.add(lblDataEspecifica);
        
        painel.add(javax.swing.Box.createVerticalStrut(10));
        
        // Dia
        spinnerDia = new JSpinner(new SpinnerNumberModel(LocalDate.now().getDayOfMonth(), 1, 31, 1));
        spinnerDia.setMaximumSize(new Dimension(80, 30));
        spinnerDia.setAlignmentX(JSpinner.CENTER_ALIGNMENT);
        spinnerDia.setVisible(false);
        
        // Mês
        comboMes = new JComboBox<>(new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12});
        comboMes.setSelectedItem(LocalDate.now().getMonthValue());
        comboMes.setMaximumSize(new Dimension(80, 30));
        comboMes.setAlignmentX(JComboBox.CENTER_ALIGNMENT);
        comboMes.setVisible(false);
        
        // Ano
        int anoAtual = LocalDate.now().getYear();
        Integer[] anos = new Integer[10];
        for (int i = 0; i < 10; i++) {
            anos[i] = anoAtual - i;
        }
        comboAno = new JComboBox<>(anos);
        comboAno.setMaximumSize(new Dimension(100, 30));
        comboAno.setAlignmentX(JComboBox.CENTER_ALIGNMENT);
        comboAno.setVisible(false);
        
        // Semana
        comboSemana = new JComboBox<>(new Integer[]{1, 2, 3, 4});
        comboSemana.setMaximumSize(new Dimension(80, 30));
        comboSemana.setAlignmentX(JComboBox.CENTER_ALIGNMENT);
        comboSemana.setVisible(false);
        
        painel.add(spinnerDia);
        painel.add(javax.swing.Box.createVerticalStrut(5));
        painel.add(comboMes);
        painel.add(javax.swing.Box.createVerticalStrut(5));
        painel.add(comboAno);
        painel.add(javax.swing.Box.createVerticalStrut(5));
        painel.add(comboSemana);
        
        return painel;
    }
    
    private void atualizarCamposData() {
        String periodo = (String) comboPeriodo.getSelectedItem();
        
        spinnerDia.setVisible(false);
        comboMes.setVisible(false);
        comboAno.setVisible(false);
        comboSemana.setVisible(false);
        
        switch (periodo) {
            case "Diário":
                lblDataEspecifica.setText("Selecione a data:");
                spinnerDia.setVisible(true);
                comboMes.setVisible(true);
                comboAno.setVisible(true);
                break;
            case "Semanal":
                lblDataEspecifica.setText("Semana do mês:");
                comboSemana.setVisible(true);
                comboMes.setVisible(true);
                comboAno.setVisible(true);
                break;
            case "Mensal":
                lblDataEspecifica.setText("Selecione o mês:");
                comboMes.setVisible(true);
                comboAno.setVisible(true);
                break;
            case "Anual":
                lblDataEspecifica.setText("Selecione o ano:");
                comboAno.setVisible(true);
                break;
            case "Todos":
                lblDataEspecifica.setText("Todos os registros");
                break;
        }
    }
    
    private JPanel criarPainelBotoes() {
        JPanel painel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        painel.setBackground(Color.WHITE);
        
        JButton btnVisualizar = new JButton("👁 Visualizar");
        btnVisualizar.setFont(new Font("Arial", Font.BOLD, 14));
        btnVisualizar.setBackground(new Color(33, 150, 243));
        btnVisualizar.setForeground(Color.WHITE);
        btnVisualizar.setPreferredSize(new Dimension(150, 40));
        btnVisualizar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnVisualizar.setFocusPainted(false);
        btnVisualizar.addActionListener(e -> visualizarRelatorio());
        
        JButton btnPDF = new JButton("📄 Gerar PDF");
        btnPDF.setFont(new Font("Arial", Font.BOLD, 14));
        btnPDF.setBackground(new Color(76, 175, 80));
        btnPDF.setForeground(Color.WHITE);
        btnPDF.setPreferredSize(new Dimension(150, 40));
        btnPDF.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnPDF.setFocusPainted(false);
        btnPDF.addActionListener(e -> gerarPDFRelatorio());
        
        painel.add(btnVisualizar);
        painel.add(btnPDF);
        
        return painel;
    }
    
    private JPanel criarPainelVisualizacao() {
        JPanel painel = new JPanel(new BorderLayout());
        painel.setBackground(Color.WHITE);
        painel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            "Resultados",
            javax.swing.border.TitledBorder.LEFT,
            javax.swing.border.TitledBorder.TOP,
            new Font("Arial", Font.BOLD, 14)
        ));
        
        areaResultados = new JTextArea();
        areaResultados.setFont(new Font("Monospaced", Font.PLAIN, 12));
        areaResultados.setEditable(false);
        areaResultados.setMargin(new Insets(10, 10, 10, 10));
        areaResultados.setLineWrap(false);
        areaResultados.setWrapStyleWord(false);
        areaResultados.setText("Clique em 'Visualizar' para exibir o relatório na tela.");
        
        JScrollPane scroll = new JScrollPane(areaResultados);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scroll.setPreferredSize(new Dimension(800, 300));
        painel.add(scroll, BorderLayout.CENTER);
        
        return painel;
    }
    
    private void visualizarRelatorio() {
        try {
            String tipo = (String) comboTipo.getSelectedItem();
            String periodo = (String) comboPeriodo.getSelectedItem();
            
            areaResultados.setText("Carregando " + tipo + " (" + periodo + ")...\n\n");
            
            StringBuilder resultado = new StringBuilder();
            resultado.append("═══════════════════════════════════════════════════════\n");
            resultado.append("RELATÓRIO: ").append(tipo.toUpperCase()).append(" - ").append(periodo.toUpperCase()).append("\n");
            resultado.append("Data: ").append(LocalDate.now().toString()).append("\n");
            resultado.append("═══════════════════════════════════════════════════════\n\n");
            
            switch (tipo) {
                case "Produção":
                    resultado.append(gerarTextoProducao());
                    break;
                case "Estoque":
                    resultado.append(gerarTextoEstoque());
                    break;
                case "Custos":
                    resultado.append(gerarTextoCustos());
                    break;
                case "Vendas":
                    resultado.append(gerarTextoVendas());
                    break;
                case "Lucro":
                    resultado.append(gerarTextoLucro());
                    break;
            }
            
            areaResultados.setText(resultado.toString());
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao visualizar: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private String gerarTextoProducao() throws Exception {
        List<Polpa> producoes = producaoController.listarTodas();
        StringBuilder sb = new StringBuilder();
        
        int contador = 1;
        for (Polpa p : producoes) {
            int qtd = p.getQuantidadeProduzida() > 0 ? p.getQuantidadeProduzida() : p.getQuantidade();
            sb.append(String.format("%3d. %-20s %-8s  Qtd: %4d un  R$ %8.2f  %s\n", 
                contador++, 
                p.getSabor(), 
                "(" + p.getUnidade() + ")",
                qtd,
                p.getPreco(),
                p.getDataProducao()
            ));
        }
        
        sb.append("\n───────────────────────────────────────────────────────\n");
        sb.append("Total de itens produzidos: ").append(producoes.size()).append("\n");
        
        return sb.toString();
    }
    
    private String gerarTextoEstoque() throws Exception {
        List<Polpa> estoque = estoqueController.listarTodas().stream()
            .filter(p -> p.getQuantidade() > 0)
            .collect(java.util.stream.Collectors.toList());
        
        StringBuilder sb = new StringBuilder();
        int total = 0;
        
        int contador = 1;
        for (Polpa p : estoque) {
            sb.append(String.format("%3d. %-20s %-8s  Qtd: %4d un  R$ %8.2f\n", 
                contador++, 
                p.getSabor(), 
                "(" + p.getUnidade() + ")",
                p.getQuantidade(),
                p.getPreco()
            ));
            total += p.getQuantidade();
        }
        
        sb.append("\n───────────────────────────────────────────────────────\n");
        sb.append("Total em estoque: ").append(total).append(" unidades\n");
        
        return sb.toString();
    }
    
    private String gerarTextoCustos() throws Exception {
        List<Custo> custos = custoController.listarTodos();
        StringBuilder sb = new StringBuilder();
        double totalCustos = 0;
        
        int contador = 1;
        for (Custo c : custos) {
            sb.append(String.format("%3d. %-15s %-30s  R$ %8.2f  %s\n", 
                contador++, 
                c.getTipo(), 
                c.getDescricao(),
                c.getValor(),
                c.getData()
            ));
            totalCustos += c.getValor();
        }
        
        sb.append("\n───────────────────────────────────────────────────────\n");
        sb.append(String.format("Total de Custos: R$ %.2f\n", totalCustos));
        
        return sb.toString();
    }
    
    private String gerarTextoVendas() throws Exception {
        List<Saida> vendas = vendasController.listarTodas();
        StringBuilder sb = new StringBuilder();
        double totalVendas = 0;
        
        int contador = 1;
        for (Saida v : vendas) {
            double valorTotal = v.getPreco() * v.getQuantidade();
            sb.append(String.format("%3d. %-20s %-20s  Qtd: %3d  R$ %8.2f  %s\n", 
                contador++, 
                v.getCliente(), 
                v.getSabor(),
                v.getQuantidade(),
                valorTotal,
                v.getDataSaida()
            ));
            totalVendas += valorTotal;
        }
        
        sb.append("\n───────────────────────────────────────────────────────\n");
        sb.append(String.format("Total de Vendas: R$ %.2f\n", totalVendas));
        
        return sb.toString();
    }
    
    private String gerarTextoLucro() throws Exception {
        List<Saida> vendas = vendasController.listarTodas();
        List<Custo> custos = custoController.listarTodos();
        
        double totalVendas = 0;
        for (Saida v : vendas) {
            totalVendas += v.getPreco() * v.getQuantidade();
        }
        
        double totalCustos = 0;
        for (Custo c : custos) {
            totalCustos += c.getValor();
        }
        
        double lucroLiquido = totalVendas - totalCustos;
        double margemLucro = totalVendas > 0 ? (lucroLiquido / totalVendas) * 100 : 0;
        
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Total de Vendas (Receitas):     R$ %10.2f\n", totalVendas));
        sb.append(String.format("Total de Custos (Despesas):     R$ %10.2f\n", totalCustos));
        sb.append("\n───────────────────────────────────────────────────────\n");
        sb.append(String.format("Lucro Líquido:                  R$ %10.2f\n", lucroLiquido));
        sb.append(String.format("Margem de Lucro:                     %8.2f %%\n", margemLucro));
        
        return sb.toString();
    }
    
    private void gerarPDFRelatorio() {
        try {
            String tipo = (String) comboTipo.getSelectedItem();
            String periodo = (String) comboPeriodo.getSelectedItem();
            String caminho = null;
            
            switch (tipo) {
                case "Produção":
                    List<Polpa> producoes = producaoController.listarTodas();
                    caminho = com.essenciadafruta.util.RelatorioProducaoPDF.gerar(producoes, periodo);
                    break;
                case "Estoque":
                    List<Polpa> estoque = estoqueController.listarTodas().stream()
                        .filter(p -> p.getQuantidade() > 0)
                        .collect(java.util.stream.Collectors.toList());
                    caminho = com.essenciadafruta.util.RelatorioEstoquePDF.gerar(estoque);
                    break;
                case "Custos":
                    List<Custo> custos = custoController.listarTodos();
                    caminho = com.essenciadafruta.util.RelatorioCustosPDF.gerar(custos, periodo);
                    break;
                case "Vendas":
                    List<Saida> vendas = vendasController.listarTodas();
                    caminho = com.essenciadafruta.util.RelatorioVendasPDF.gerar(vendas, periodo);
                    break;
                case "Lucro":
                    double totalVendas = vendasController.listarTodas().stream()
                        .mapToDouble(v -> v.getPreco() * v.getQuantidade()).sum();
                    double totalCustos = custoController.listarTodos().stream()
                        .mapToDouble(Custo::getValor).sum();
                    caminho = com.essenciadafruta.util.RelatorioLucrosPDF.gerar(totalVendas, totalCustos, periodo);
                    break;
            }
            
            if (caminho != null) {
                abrirArquivo(caminho);
                JOptionPane.showMessageDialog(this, "✓ PDF gerado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao gerar PDF: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void gerarRelatorioProducao(java.awt.event.ActionEvent e) {
        try {
            String periodo = ((JButton) e.getSource()).getActionCommand();
            List<Polpa> producoes = producaoController.listarTodas();
            
            String caminho = com.essenciadafruta.util.RelatorioProducaoPDF.gerar(producoes, periodo);
            if (caminho != null) {
                abrirArquivo(caminho);
                JOptionPane.showMessageDialog(null, "✓ Relatório de Produção (" + periodo + ") gerado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Erro: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void gerarRelatorioEstoque() {
        try {
            List<Polpa> todasPolpas = estoqueController.listarTodas();
            
            // Filtrar apenas itens com quantidade maior que zero
            List<Polpa> estoque = todasPolpas.stream()
                .filter(p -> p.getQuantidade() > 0)
                .collect(java.util.stream.Collectors.toList());
            
            String caminho = com.essenciadafruta.util.RelatorioEstoquePDF.gerar(estoque);
            if (caminho != null) {
                abrirArquivo(caminho);
                JOptionPane.showMessageDialog(null, "✓ Relatório de Estoque gerado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Erro: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void gerarRelatorioCustos(java.awt.event.ActionEvent e) {
        try {
            String periodo = ((JButton) e.getSource()).getActionCommand();
            List<Custo> custos = custoController.listarTodos();
            
            String caminho = com.essenciadafruta.util.RelatorioCustosPDF.gerar(custos, periodo);
            if (caminho != null) {
                abrirArquivo(caminho);
                JOptionPane.showMessageDialog(null, "✓ Relatório de Custos (" + periodo + ") gerado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Erro: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void gerarRelatorioVendas(java.awt.event.ActionEvent e) {
        try {
            String periodo = ((JButton) e.getSource()).getActionCommand();
            List<Saida> vendas = vendasController.listarTodas();
            
            String caminho = com.essenciadafruta.util.RelatorioVendasPDF.gerar(vendas, periodo);
            if (caminho != null) {
                abrirArquivo(caminho);
                JOptionPane.showMessageDialog(null, "✓ Relatório de Vendas (" + periodo + ") gerado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Erro: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void gerarRelatorioLucro(java.awt.event.ActionEvent e) {
        try {
            String periodo = ((JButton) e.getSource()).getActionCommand();
            
            double totalVendas = 0;
            double totalCustos = 0;
            
            try {
                for (Saida venda : vendasController.listarTodas()) {
                    totalVendas += venda.getValorTotal();
                }
            } catch (Exception ex) {}
            
            try {
                for (Custo custo : custoController.listarTodos()) {
                    totalCustos += custo.getValor();
                }
            } catch (Exception ex) {}
            
            String caminho = com.essenciadafruta.util.RelatorioLucrosPDF.gerar(totalVendas, totalCustos, periodo);
            if (caminho != null) {
                abrirArquivo(caminho);
                JOptionPane.showMessageDialog(null, "✓ Relatório de Lucro (" + periodo + ") gerado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Erro: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void abrirArquivo(String caminho) {
        try {
            if (System.getProperty("os.name").toLowerCase().contains("win")) {
                Runtime.getRuntime().exec("explorer.exe /select,\"" + caminho + "\"");
            } else if (System.getProperty("os.name").toLowerCase().contains("mac")) {
                Runtime.getRuntime().exec(new String[]{"open", "-R", caminho});
            } else {
                Runtime.getRuntime().exec(new String[]{"xdg-open", new File(caminho).getParent()});
            }
        } catch (Exception ex) {
            System.err.println("Erro ao abrir arquivo: " + ex.getMessage());
        }
    }
}