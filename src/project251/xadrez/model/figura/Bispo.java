package project251.xadrez.model.figura;

import project251.xadrez.model.tabuleiro.Posicao;

public class Bispo extends Peca {

	public Bispo(Posicao posicao) {
		super(posicao);
	}
	
	@Override
	public String toString() {
	    return "B";
	}
	
}
