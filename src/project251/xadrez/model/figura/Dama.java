package project251.xadrez.model.figura;

import java.util.ArrayList;

import project251.xadrez.model.tabuleiro.Posicao;

public class Dama extends Peca {

	public Dama(Posicao posicao, int cor) {
		super(posicao, cor);
	}
	
	@Override
	public String toString() {
	    return "D";
	}
	
	@Override
	public ArrayList<Posicao> movValidos() {
		return null;
	}

}
