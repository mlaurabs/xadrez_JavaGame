package project251.xadrez.model.figura;

import java.util.ArrayList;

import project251.xadrez.model.tabuleiro.Posicao;
import project251.xadrez.model.tabuleiro.Tabuleiro;

public class Cavalo extends Peca {

	public Cavalo(Posicao posicao, int cor) {
		super(posicao, cor);
	}

	@Override
	public String toString() {
	    return "C"+this.getCor();
	}
	
	// para cada movimento, temos que verificar se não é um movimento ilegal
	// movimentos ilegais: o rei está em xeque, o rei fica em xeque após o seu movimento
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

	    return movimentos;
	}

}
