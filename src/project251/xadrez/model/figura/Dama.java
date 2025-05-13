package project251.xadrez.model.figura;

import project251.xadrez.model.tabuleiro.Posicao;

public class Dama extends Peca {

	public Dama(Posicao posicao) {
		super(posicao);
	}
	
	@Override
	public String toString() {
	    return "D";
	}

}
