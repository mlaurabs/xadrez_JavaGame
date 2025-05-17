package project251.xadrez.model.figura;

import java.util.ArrayList;
import project251.xadrez.model.tabuleiro.*;

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
	    return "P"+this.getCor();
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
        }
        else {
        	mov_cor = -1; //se as peças forem pretas elas andar 'para tras' em relação ao tabuleiro das brancas
        }
        
        Posicao frente = new Posicao(linha + 1*mov_cor, coluna);
        if (tabuleiro.posicaoExiste(frente)) {
            if (!tabuleiro.existePeca(frente)) { //se não tiver nenhuma peça na frente, o peão sempre pode
            	movimentos.add(frente); 		// andar uma casa
            }
        }
        
        Posicao duas_frente = new Posicao(linha + 2*mov_cor, coluna);
        if (tabuleiro.posicaoExiste(duas_frente)) {
            if (!tabuleiro.existePeca(frente) & !tabuleiro.existePeca(duas_frente)) {
                if(!this.getJaMoveu()) { //se não tiver peça na frente e ainda não moveu, o peão pode andar 2 casas
                	movimentos.add(duas_frente);
                }
            }
        }

        Posicao diag_dir = new Posicao(linha + mov_cor, coluna + 1);
        if (tabuleiro.posicaoExiste(diag_dir)) {
            if (tabuleiro.existePeca(diag_dir)) { //se houver uma peça da cor oposta na diagonal o peão pode capturar
                if (tabuleiro.getPeca(diag_dir).cor != this.cor) {
                    movimentos.add(diag_dir);
                }
            }
        }

        Posicao diag_esq = new Posicao(linha + mov_cor, coluna - 1);
        if (tabuleiro.posicaoExiste(diag_esq)) {
            if (tabuleiro.existePeca(diag_esq)) { //se houver uma peça da cor oposta na diagonal o peão pode capturar
                if (tabuleiro.getPeca(diag_esq).cor != this.cor) {
                    movimentos.add(diag_esq);
                }
            }
        };
        

		return movimentos;
	}

	@Override
	public String getTipoPeca() {
		return "P";
	}
}
