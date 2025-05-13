package project251.xadrez.model.figura;

import project251.xadrez.model.tabuleiro.Posicao;

public class Rei extends Peca {
	private boolean jaMoveu = false; 

	public Rei(Posicao posicao) {
		super(posicao);
	}

	public boolean getJaMoveu() { //se falso, pode fazer o roque
		return jaMoveu;
	}

	public void setJaMoveu(boolean jaMoveu) {
		this.jaMoveu = jaMoveu;
	}
	
	@Override
	public String toString() {
	    return "R";
	}
}
