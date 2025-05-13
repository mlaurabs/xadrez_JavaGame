package project251.xadrez.model.figura;

import java.util.ArrayList;

import project251.xadrez.model.tabuleiro.Posicao;

public class Rei extends Peca {
	private boolean jaMoveu = false; 

	public Rei(Posicao posicao, int cor) {
		super(posicao, cor);
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
	
	@Override
	public ArrayList<Posicao> movValidos() {
		return null;
	}
}
