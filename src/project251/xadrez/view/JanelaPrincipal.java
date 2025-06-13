package project251.xadrez.view;

import javax.swing.*;

import project251.xadrez.controller.Controller;
import project251.xadrez.model.Jogador;

import java.awt.*;
import java.awt.event.ActionEvent;

public class JanelaPrincipal extends JFrame {

    private CardLayout cardLayout;
    private JPanel painelPrincipal;
    private JPanel telaInicial;
    private PainelTabuleiro painelTabuleiro;
    private Controller controller;

    public JanelaPrincipal() {
        super("Jogo de Xadrez - Projeto INF1636");

        configurarJanela();
        inicializarComponentes();

        setVisible(true);
        
     
    }

    private void configurarJanela() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(640, 680);
        setLocationRelativeTo(null); // Centraliza
        setResizable(false);
    }

    private void inicializarComponentes() {
        cardLayout = new CardLayout();
        painelPrincipal = new JPanel(cardLayout);

     // Cria o controller primeiro
        this.controller = new Controller();
        
        criarTelaInicial();
        painelTabuleiro = new PainelTabuleiro(controller); 

        painelPrincipal.add(telaInicial, "TelaInicial");
        painelPrincipal.add(painelTabuleiro, "Tabuleiro");

        add(painelPrincipal);
        cardLayout.show(painelPrincipal, "TelaInicial");
    }
    
    private void encerrarPartida() {
        if (painelTabuleiro != null) {
            painelTabuleiro.encerrar(); // limpa recursos da view
        }
        Jogador.reiniciarPlacar();
        setJMenuBar(null); // remove a barra de menu
        cardLayout.show(painelPrincipal, "TelaInicial");
    }


    private JMenuBar criarMenuPartida() {
        JMenuBar menuBar = new JMenuBar();

        // Título como JLabel
        JLabel titulo = new JLabel("  Xad Estratégia  ");
        titulo.setFont(new Font("Arial", Font.BOLD, 16));
        titulo.setForeground(new Color(80, 0, 80)); // Roxo escuro
        menuBar.add(titulo);

        // Espaço entre título e primeiro botão
        menuBar.add(Box.createHorizontalStrut(20));

        // Botão: Ver Placar
        JButton btnVerPlacar = new JButton("Ver Placar");
        btnVerPlacar.setFocusable(false);
        btnVerPlacar.addActionListener(e -> {
            StringBuilder placar = new StringBuilder();
            placar.append("PLACAR ATUAL:\n\n");

            String cyon = Jogador.C.getPecasCapturadasString().replace("C: ", "");
            String purple = Jogador.P.getPecasCapturadasString().replace("P: ", "");

            placar.append("Cyon (C): ").append(cyon.isEmpty() ? "Nenhuma peça capturada" : cyon).append("\n");
            placar.append("Purple (P): ").append(purple.isEmpty() ? "Nenhuma peça capturada" : purple);

            JOptionPane.showMessageDialog(this, placar.toString(), "Placar", JOptionPane.INFORMATION_MESSAGE);
        });
        menuBar.add(btnVerPlacar);

        // Espaço entre os botões
        menuBar.add(Box.createHorizontalStrut(20));

        // Botão: Encerrar Partida
        JButton btnEncerrar = new JButton("Encerrar Partida");
        btnEncerrar.setFocusable(false);
        btnEncerrar.addActionListener(e -> encerrarPartida());
        menuBar.add(btnEncerrar);

        return menuBar;
    }


    
    private void criarTelaInicial() {
        telaInicial = new JPanel();
        telaInicial.setLayout(new BoxLayout(telaInicial, BoxLayout.Y_AXIS));
        telaInicial.setBackground(new Color(224,255,255));

        // Carrega a imagem do logo
        ImageIcon logo = new ImageIcon(getClass().getClassLoader().getResource("images/logo_xadrez.png"));
        JLabel titulo = new JLabel(logo);
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        titulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0)); // Margem ajustada


        Color corFundo = new Color(133, 12, 100); 
        Color corTexto = Color.WHITE;

        JButton btnNovoJogo = new JButton("Novo Jogo");
        btnNovoJogo.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnNovoJogo.setMaximumSize(new Dimension(200, 60));
        btnNovoJogo.setBackground(corFundo);
        btnNovoJogo.setForeground(corTexto);
        btnNovoJogo.setFocusPainted(false); // Remove a borda de foco azul
        btnNovoJogo.setFont(new Font("Arial", Font.BOLD, 16));
        btnNovoJogo.addActionListener((ActionEvent e) -> iniciarNovoJogo());

        JButton btnCarregar = new JButton("Carregar Jogo");
        btnCarregar.setEnabled(false); // Ativar na 4ª iteração
        btnCarregar.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnCarregar.setMaximumSize(new Dimension(200, 60));
        btnCarregar.setBackground(corFundo);
        btnCarregar.setForeground(corTexto);
        btnCarregar.setFocusPainted(false);
        btnCarregar.setFont(new Font("Arial", Font.BOLD, 16));

        telaInicial.add(Box.createVerticalGlue());
        telaInicial.add(titulo);
        telaInicial.add(btnNovoJogo);
        telaInicial.add(Box.createRigidArea(new Dimension(0, 20)));
        telaInicial.add(btnCarregar);
        telaInicial.add(Box.createVerticalGlue());
    }

    private void iniciarNovoJogo() {
        setJMenuBar(criarMenuPartida()); // <-- define a barra de menu
        cardLayout.show(painelPrincipal, "Tabuleiro");
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(JanelaPrincipal::new);
    }
}
