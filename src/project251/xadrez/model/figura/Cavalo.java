package project251.xadrez.model.figura;

import java.util.ArrayList;

import project251.xadrez.model.tabuleiro.Posicao;

public class Cavalo extends Peca {

	public Cavalo(Posicao posicao, int cor) {
		super(posicao, cor);
	}

	@Override
	public String toString() {
	    return "C";
	}
	
	@Override
	public ArrayList<Posicao> movValidos() {
		return null;
	}
}
