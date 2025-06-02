package project251.xadrez.view;

import project251.xadrez.controller.Controller;
import project251.xadrez.model.Posicao;
import project251.xadrez.model.TabuleiroObserver;
import project251.xadrez.model.XadrezFacade;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class PainelTabuleiro extends JPanel implements TabuleiroObserver{
    private static final int TAM_CASA = 80;
    private static final Color COR_CLARA = new Color(255, 255, 255);
    private static final Color COR_ESCURA = new Color(0, 0, 0);
    private final Controller controller;
    private ArrayList<Posicao> movimentosValidos = new ArrayList<>();
    private String[][] estadoTabuleiro;
    private String[][] tabuleiro = new String[8][8];

    public PainelTabuleiro(Controller  controller) {
    	this.controller = controller;
    	this.controller.registrarObserver(this); // Registra-se via Controller
    	this.estadoTabuleiro = controller.getEstadoTabuleiro();

    	
        setPreferredSize(new Dimension(8 * TAM_CASA, 8 * TAM_CASA));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int linha = e.getY() / TAM_CASA;
                int coluna = e.getX() / TAM_CASA;

             // Verifica se o botão direito do mouse foi clicado
                if (SwingUtilities.isRightMouseButton(e)) {
                    // Abrir JFileChooser para salvar o estado do jogo
                    JFileChooser chooser = new JFileChooser();
                    int resultado = chooser.showSaveDialog(null);
                    
                    if (resultado == JFileChooser.APPROVE_OPTION) {
                        java.io.File arquivo = chooser.getSelectedFile();
                        controller.salvarEstadoJogo(arquivo);
                    }
                    return; // Retorna para evitar outros comportamentos ao clicar com o botão direito
                }

                controller.processarClique(linha, coluna);

            }
        });
    }
    
    public void encerrar() {
        // Libere listeners, threads, timers, etc.
        this.removeAll();
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        for (int linha = 7; linha >= 0; linha--) {
            for (int coluna = 0; coluna < 8; coluna++) {
                boolean casaEscura = (linha + coluna) % 2 != 0;
                g2.setColor(casaEscura ? COR_ESCURA : COR_CLARA);
                int x = coluna * TAM_CASA;
                int y = linha * TAM_CASA;
                g2.fillRect(x, y, TAM_CASA, TAM_CASA);

                // Pinta a peça (se houver)
                String nomePeca = estadoTabuleiro[linha][coluna];
                if (nomePeca != null) {
                    BufferedImage imagem = ImagemPecas.getImagem(nomePeca);
                    if (imagem != null) {
                        g2.drawImage(imagem, x, y, TAM_CASA, TAM_CASA, null);
                    }
                }
            }
        }

        if (movimentosValidos != null && !movimentosValidos.isEmpty()) {
        	g2.setColor(new Color(105, 35, 235, 200)); 
            for (Posicao pos : movimentosValidos) {
 
                int x = pos.getColuna() * TAM_CASA;
                int y = pos.getLinha() * TAM_CASA;
                g2.fillRect(x, y, TAM_CASA, TAM_CASA);
            }
        }
    }

	/*
	 * public void iniciarNovoJogo() { inicializarTabuleiro(); repaint(); }
	 */


    @Override
    public void atualizar() {
        // Atualiza movimentos e repinta na thread da UI
        SwingUtilities.invokeLater(() -> {
        	estadoTabuleiro = controller.getEstadoTabuleiro();
        	movimentosValidos = controller.obterMovimentosValidos();
            repaint();
        });
    }


}
