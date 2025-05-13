package project251.xadrez.model.figura;

import project251.xadrez.model.tabuleiro.Posicao;

public class Torre extends Peca {

	public Torre(Posicao posicao) {
		super(posicao);
	}
	
	@Override
	public String toString() {
	    return "T";
	}

}
