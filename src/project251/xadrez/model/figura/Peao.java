package project251.xadrez.model.figura;

import java.util.ArrayList;

import project251.xadrez.model.tabuleiro.Posicao;

public class Peao extends Peca {
	private boolean jaMoveu = false; 

	public Peao(Posicao posicao, int cor) {
		super(posicao, cor);
	}

	public boolean getJaMoveu() { // se falso, pode andar duas casas na primeira rodada
		return jaMoveu;
	}


	public void setJaMoveu(boolean jaMoveu) {
		this.jaMoveu = jaMoveu;
	}
	

	@Override
	public String toString() {
	    return "P";
	}
	
	@Override
	public ArrayList<Posicao> movValidos() {
		return null;
	}
}
