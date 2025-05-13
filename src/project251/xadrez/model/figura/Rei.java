package project251.xadrez.model.figura;

import project251.xadrez.model.tabuleiro.Posicao;

public class Rei extends Peca {

	public Rei(Posicao posicao) {
		super(posicao);
	}
	
	@Override
	public String toString() {
	    return "R";
	}
}
