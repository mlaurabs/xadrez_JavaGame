package project251.xadrez.model.figura;

import java.util.ArrayList;
import project251.xadrez.model.tabuleiro.Posicao;

public class Bispo extends Peca {

	public Bispo(Posicao posicao, int cor) {
		super(posicao, cor);
	}
	
	@Override
	public String toString() {
	    return "B";
	}

	@Override
	public ArrayList<Posicao> movValidos() {
		return null;
	}
	
	
}
