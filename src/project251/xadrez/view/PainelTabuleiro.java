package project251.xadrez.view;

import project251.xadrez.controller.Controller;
import project251.xadrez.model.Posicao;
import project251.xadrez.model.XadrezFacade;


import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class PainelTabuleiro extends JPanel {

    private static final int TAM_CASA = 80;
    private static final Color COR_CLARA = new Color(255, 255, 255);
    private static final Color COR_ESCURA = new Color(0, 0, 0);
    private final Controller controller = new Controller();


    // Exemplo de estrutura para o tabuleiro (a ser substituído futuramente por XadrezFacade)
    private String[][] tabuleiro = new String[8][8];

    public PainelTabuleiro() {
        setPreferredSize(new Dimension(8 * TAM_CASA, 8 * TAM_CASA));
        inicializarTabuleiro();

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
                        Controller.salvarEstadoJogo(XadrezFacade.getInstance(), arquivo); // Salvar estado do jogo
                    }
                    return; // Retorna para evitar outros comportamentos ao clicar com o botão direito
                }

                // Caso contrário, processa o clique normal
                controller.processarClique(linha, coluna, () -> repaint());
            }
        });
    }



    private void inicializarTabuleiro() {

    	// Peças Cian (Cyan) - Time 1 (linha 6 e 7)
    	tabuleiro[7][0] = "CyanR"; // Torre
    	tabuleiro[7][1] = "CyanN"; // Cavalo
    	tabuleiro[7][2] = "CyanB"; // Bispo
    	tabuleiro[7][3] = "CyanQ"; // Rainha
    	tabuleiro[7][4] = "CyanK"; // Rei
    	tabuleiro[7][5] = "CyanB"; // Bispo
    	tabuleiro[7][6] = "CyanN"; // Cavalo
    	tabuleiro[7][7] = "CyanR"; // Torre

    	// Peões Cian (linha 6)
    	for (int col = 0; col < 8; col++) {
    		tabuleiro[6][col] = "CyanP"; // Peões
    	}

    	// Peças Roxas (Purple) - Time 2 (linha 6 e 7)
    	tabuleiro[0][0] = "PurpleR"; // Torre
    	tabuleiro[0][1] = "PurpleN"; // Cavalo
    	tabuleiro[0][2] = "PurpleB"; // Bispo
    	tabuleiro[0][3] = "PurpleQ"; // Rainha
    	tabuleiro[0][4] = "PurpleK"; // Rei
    	tabuleiro[0][5] = "PurpleB"; // Bispo
    	tabuleiro[0][6] = "PurpleN"; // Cavalo
    	tabuleiro[0][7] = "PurpleR"; // Torre

    	// Peões Roxos (linha 1)
    	for (int col = 0; col < 8; col++) {
    		tabuleiro[1][col] = "PurpleP"; // Peões
    	}

    	
    	// Espaços vazios (linhas 2 a 5)
    	for (int row = 2; row < 6; row++) {
    	    for (int col = 0; col < 8; col++) {
    	    	tabuleiro[row][col] = null; // ou "" para vazio
    	    }
    	}
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        
        // Recupera os movimentos válidos da peça selecionada
        ArrayList<Posicao> destaques = controller.obterMovimentosValidos();
        
        if(destaques != null) { // isso aqui não deu certo pra pintar as casas que representam movimentos válidos
        	System.out.println("aqui");
        	for (Posicao pos : destaques) {
                int linhaTela = 7 - pos.getLinha(); // para exibir corretamente
                int x = pos.getColuna() * TAM_CASA;
                int y = linhaTela * TAM_CASA;

                g2.setColor(new Color(255, 182, 255)); // rosa claro transparente
                g2.fillRect(x, y, TAM_CASA, TAM_CASA);
            }
        }
        
        
        for (int linha = 7; linha >= 0; linha--) {
            for (int coluna = 0; coluna < 8; coluna++) {
                boolean casaEscura = (linha + coluna) % 2 != 0;
                g2.setColor(casaEscura ? COR_ESCURA : COR_CLARA);
                g2.fillRect(coluna * TAM_CASA, linha * TAM_CASA, TAM_CASA, TAM_CASA);

                String nomePeca = tabuleiro[linha][coluna];
                if (nomePeca != null) {
                    BufferedImage imagem = ImagemPecas.getImagem(nomePeca);
                    if (imagem != null) {
                        g2.drawImage(imagem, coluna * TAM_CASA, linha * TAM_CASA, TAM_CASA, TAM_CASA, null);
                    }
                }
            }
        }
    }

    public void iniciarNovoJogo() { // desenvolver para próximas ietarações
        inicializarTabuleiro(); 
        repaint();
    }
}
