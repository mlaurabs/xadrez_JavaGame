package project251.xadrez.model;

import java.util.ArrayList;


/**
 * Representa a peça Peão no jogo de xadrez. Implementa movimentos especiais
 * como avanço inicial de duas casas e captura em diagonal.
 */
class Peao extends Peca {
	
	/** Indica se o peão já realizou seu primeiro movimento */
	private boolean jaMoveu = false; 

	/**
     * Cria um novo peão na posição especificada.
     * @param posicao (Posição) inicial do peão no tabuleiro
     * @param cor (Cor) da peça (0 para brancas, 1 para pretas)
     */
	public Peao(Posicao posicao, int cor) {
		super(posicao, cor);
	}

	 /**
     * Verifica se o peão já realizou seu primeiro movimento.
     * @return true se o peão já se moveu, false caso contrário
     */
	public boolean getJaMoveu() { // se falso, pode andar duas casas na primeira rodada
		return jaMoveu;
	}

	/**
     * Define o estado de movimento do peão.
     * @param jaMoveu true se o peão já realizou seu primeiro movimento
     */
	public void setJaMoveu(boolean jaMoveu) {
		this.jaMoveu = jaMoveu;
	}
	
	/**
     * Retorna a representação textual do peão.
     * @return String no formato "P" seguido da cor (ex: "P0" para peão branco)
     */
	@Override
	public String toString() {
	    return "P"+this.getCor();
	}
	

    /**
     * Calcula todos os movimentos válidos para o peão no tabuleiro atual.
     * @param tabuleiro Tabuleiro atual do jogo
     * @return Lista de posições válidas para movimento
     */
	@Override
	public ArrayList<Posicao> movValidos(Tabuleiro tabuleiro) {
		ArrayList<Posicao> movimentos = new ArrayList<>();
        int linha = posicao.getLinha();
        int coluna = posicao.getColuna();
        int mov_cor;
        
        if (this.cor == 0) { //se as peças forem cyon elas andam pra frente
        	System.out.println("entrei");
        	mov_cor = -1; 
        }
        else {
        	mov_cor = +1; //se as peças forem purple elas andar 'para tras' em relação ao tabuleiro das cyon
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
        
        this.movimentos = movimentos;
		return movimentos;
	}

	
	/**
     * Retorna o tipo da peça.
     * @return "P" (identificador de peão)
     */
	@Override
	public String getTipoPeca() {
		return "P";
	}

	 /**
     * Cria uma cópia exata deste peão.
     * @param tabuleiro (Tabuleiro) de referência para cálculo de movimentos
     * @return Nova instância de Peao com mesmo estado
     */
    public Peca clonar(Tabuleiro tabuleiro) {
        Peao clone = new Peao(posicao, cor);
        clone.setPosicao(new Posicao(this.posicao.getLinha(), this.posicao.getColuna()));
        ArrayList<Posicao> movimentos = clone.movValidos(tabuleiro);
        clone.setMovimentos(movimentos);
        return clone;
    }
}
