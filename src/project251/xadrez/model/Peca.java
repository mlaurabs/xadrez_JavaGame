package project251.xadrez.model;

import java.util.ArrayList;

/**
 * Classe abstrata que representa uma peça genérica do xadrez.
 * Define a estrutura básica e comportamentos comuns a todas as peças do jogo.
 */
public abstract class Peca {
	 /** Posição atual da peça no tabuleiro */
    public Posicao posicao;
    
    /** Cor da peça (0 para brancas, 1 para pretas) */
    public int cor;
    
    /** Lista de movimentos válidos calculados para esta peça */
    public ArrayList<Posicao> movimentos = new ArrayList<Posicao>();
    // 0 pras brancas 
    // 1 pras pretas

    
    /**
     * Construtor base para todas as peças.
     * @param posicao Posição inicial da peça no tabuleiro
     * @param cor Cor da peça (0 para brancas, 1 para pretas)
     */
    public Peca(Posicao posicao, int cor) {
        this.posicao = posicao;
        this.cor = cor;
        this.movimentos = new ArrayList<Posicao>();
    }
    

    public void setMovimentos(ArrayList<Posicao> movimentos) {
        this.movimentos = movimentos;
    }

    /**
     * Obtém a posição atual da peça.
     * @return objeto Posicao com as coordenadas atuais
     */
    public Posicao getPosicao() {
        return posicao;
    }

    /**
     * Define uma nova posição para a peça.
     * @param posicao Nova posição no tabuleiro
     */
    
    public void setPosicao(Posicao posicao) {
        this.posicao = posicao;
    }
    
    /**
     * Retorna a representação textual da cor da peça.
     * @return "C" para cyon, "P" para purple
     */
    public String getCor() {
        if (cor == 0) {
            return "C";
        } else {
            return "P";
        }
    }

    /**
     * Altera a cor da peça.
     * @param cor Nova cor (0 para brancas, 1 para pretas)
     */
    public void setCor(int cor) {
        this.cor = cor;
    }
    
    /**
     * Método abstrato para obter o tipo da peça.
     * @return Letra identificadora da peça (ex: "R" para Rei)
     */
    public abstract String getTipoPeca();
    
    /**
     * Método abstrato para calcular movimentos válidos.
     * @param tabuleiro Referência do tabuleiro atual
     * @return Lista de posições válidas para movimento
     */ 
    public abstract ArrayList<Posicao> movValidos(Tabuleiro tabuleiro);
    
    
    /**
     * Método abstrato para clonagem de peças.
     * @param tabuleiro Referência do tabuleiro para cálculo de movimentos
     * @return Nova instância da peça com mesmo estado
     */
    public abstract Peca clonar(Tabuleiro tabuleiro);
}
