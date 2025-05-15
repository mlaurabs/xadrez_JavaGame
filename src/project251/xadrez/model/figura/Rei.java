package project251.xadrez.model.figura;

import java.util.ArrayList;

import project251.xadrez.model.tabuleiro.Posicao;
import project251.xadrez.model.tabuleiro.Tabuleiro;

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
	    return "R"+this.getCor();
	}
	
	// para cada movimento, temos que verificar se não é um movimento ilegal
	// movimentos ilegais: o rei está em xeque, o rei fica em xeque após o seu movimento
	@Override
	public ArrayList<Posicao> movValidos(Tabuleiro tabuleiro) {
	    ArrayList<Posicao> movimentos = new ArrayList<>();
	    int linha = posicao.getLinha();
	    int coluna = posicao.getColuna();
	    int mov_cor;

	    if (this.cor == 0) { //se as peças forem brancas elas andam pra frente
	        mov_cor = 1; 
	    } else {
	        mov_cor = -1; //se as peças forem pretas elas andar 'para tras' em relação ao tabuleiro das brancas
	    }

	    Posicao frente = new Posicao(linha + 1*mov_cor, coluna);
	    if (tabuleiro.posicaoExiste(frente)) {
	        if (!tabuleiro.existePeca(frente)) { // andar pra frente
	            movimentos.add(frente);        
	        } else {
	            if (this.cor != tabuleiro.getPeca(frente).cor) {
	                movimentos.add(frente);
	            }
	        }
	    }

	    Posicao tras = new Posicao(linha - 1*mov_cor, coluna);
	    if (tabuleiro.posicaoExiste(tras)) {
	        if (!tabuleiro.existePeca(tras)) { // andar pra tras
	            movimentos.add(tras);        
	        } else {
	            if (this.cor != tabuleiro.getPeca(tras).cor) {
	                movimentos.add(tras); 
	            }
	        }
	    }

	    Posicao dir = new Posicao(linha, coluna + 1*mov_cor);
	    if (tabuleiro.posicaoExiste(dir)) {
	        if (!tabuleiro.existePeca(dir)) { // andar pra frente
	            movimentos.add(dir);        
	        } else {
	            if (this.cor != tabuleiro.getPeca(dir).cor) {
	                movimentos.add(dir); 
	            }
	        }
	    }

	    Posicao esq = new Posicao(linha, coluna - 1*mov_cor);
	    if (tabuleiro.posicaoExiste(esq)) {
	        if (!tabuleiro.existePeca(esq)) { // andar pra frente
	            movimentos.add(esq);        
	        } else {
	            if (this.cor != tabuleiro.getPeca(esq).cor) {
	                movimentos.add(esq); 
	            }
	        }
	    }

	    Posicao diag_dir_frente = new Posicao(linha + 1*mov_cor, coluna + 1*mov_cor);
	    if (tabuleiro.posicaoExiste(diag_dir_frente)) {
	        if (!tabuleiro.existePeca(diag_dir_frente)) { // andar pra frente
	            movimentos.add(diag_dir_frente);        
	        } else {
	            if (this.cor != tabuleiro.getPeca(diag_dir_frente).cor) {
	                movimentos.add(diag_dir_frente); 
	            }
	        }
	    }

	    Posicao diag_esq_frente = new Posicao(linha + 1*mov_cor, coluna - 1*mov_cor);
	    if (tabuleiro.posicaoExiste(diag_esq_frente)) {
	        if (!tabuleiro.existePeca(diag_esq_frente)) { // andar pra frente
	            movimentos.add(diag_esq_frente);        
	        } else {
	            if (this.cor != tabuleiro.getPeca(diag_esq_frente).cor) {
	                movimentos.add(diag_esq_frente); 
	            }
	        }
	    }

	    Posicao diag_dir_tras = new Posicao(linha - 1*mov_cor, coluna + 1*mov_cor);
	    if (tabuleiro.posicaoExiste(diag_dir_tras)) {
	        if (!tabuleiro.existePeca(diag_dir_tras)) { // andar pra frente
	            movimentos.add(diag_dir_tras);        
	        } else {
	            if (this.cor != tabuleiro.getPeca(diag_dir_tras).cor) {
	                movimentos.add(diag_dir_tras); 
	            }
	        }
	    }

	    Posicao diag_esq_tras  = new Posicao(linha - 1*mov_cor, coluna - 1*mov_cor);
	    if (tabuleiro.posicaoExiste(diag_esq_tras)) {
	        if (!tabuleiro.existePeca(diag_esq_tras)) { // andar pra frente
	            movimentos.add(diag_esq_tras);        
	        } else {
	            if (this.cor != tabuleiro.getPeca(diag_esq_tras).cor) {
	                movimentos.add(diag_esq_tras); 
	            }
	        }
	    }

	    return movimentos;
	}

}

