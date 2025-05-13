package project251.xadrez.model.figura;

import project251.xadrez.model.tabuleiro.Posicao;

public class Peao extends Peca {

	public Peao(Posicao posicao) {
		super(posicao);
	}

	@Override
	public String toString() {
	    return "P";
	}
}
