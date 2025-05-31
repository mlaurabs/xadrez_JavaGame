package project251.xadrez.model;

import java.util.ArrayList;


/**
 * Representa a peça Cavalo no jogo de xadrez.
 * Controla movimentos válidos, estado de movimento e clonagem.
 */
class Cavalo extends Peca {

	public Cavalo(Posicao posicao, int cor) {
		super(posicao, cor);
	}
	
	/**
     * Retorna a representação textual do cavalo.
     * @return String no formato "C" seguido da cor (ex: "C0" para cavalo branco)
     */
	@Override
	public String toString() {
	    return "C"+this.getCor();
	}
	
	/**
	 * Calcula todos os movimentos válidos para o cavalo no tabuleiro atual.
	 * O cavalo se move em "L": duas casas em uma direção (horizontal ou vertical)
	 * e depois uma casa em uma direção lateral. Podendo passar por cima de outras peças.
	 * 
	 * @param tabuleiro Tabuleiro atual do jogo
	 * @return Lista de posições válidas para movimento
	 */
	@Override
	public ArrayList<Posicao> movValidos(Tabuleiro tabuleiro) {
	    ArrayList<Posicao> movimentos = new ArrayList<>();
	    int linha = posicao.getLinha();
	    int coluna = posicao.getColuna();

	    // Movimentos possíveis para o cavalo (em "L")
	    int[][] direcoes = {
	        {2, 1}, {2, -1}, {-2, 1}, {-2, -1}, // Movimentos com 2 casas na vertical
	        {1, 2}, {1, -2}, {-1, 2}, {-1, -2}  // Movimentos com 2 casas na horizontal
	    };

	    // Para cada direção do cavalo
	    for (int[] direcao : direcoes) {
	        int novaLinha = linha + direcao[0];
	        int novaColuna = coluna + direcao[1];
	        Posicao novaPosicao = new Posicao(novaLinha, novaColuna);

	        // Verifica se a posição existe no tabuleiro
	        if (tabuleiro.posicaoExiste(novaPosicao)) {
	            if (!tabuleiro.existePeca(novaPosicao) || this.cor != tabuleiro.getPeca(novaPosicao).cor) {
	                movimentos.add(novaPosicao);
	            }
	        }
	    }
		this.movimentos = movimentos;
	    return movimentos;
	}

	/**
     * Retorna o tipo da peça.
     * @return "C" (identificador do cavalo)
     */
	@Override
	public String getTipoPeca() {
		return "C";
	}

	
	/**
     * Cria uma cópia exata do cavalo.
     * @param tabuleiro Tabuleiro de referência para cálculo de movimentos
     * @return Nova instância de Cavalo com mesmo estado
     */
    public Peca clonar(Tabuleiro tabuleiro) {
        Cavalo clone = new Cavalo(posicao, cor);
        clone.setPosicao(new Posicao(this.posicao.getLinha(), this.posicao.getColuna()));
		ArrayList<Posicao> movimentos = clone.movValidos(tabuleiro);
		clone.setMovimentos(movimentos);
        return clone;
    }

}
