package project251.xadrez.model;

import java.util.ArrayList;

/**
 * Representa a peça Rei no jogo de xadrez.
 * Implementa movimentos especiais como roque e verificação de xeque.
 */
class Rei extends Peca {

	/** Indica se o rei está em xeque */
	private boolean estaXeque = false;
	
	/** Indica se o rei pode sair do xeque */
	public boolean saiXeque = true;
	
	
	/**
     * Cria um novo rei na posição especificada.
     * @param posicao (Posição) inicial do rei no tabuleiro
     * @param cor (Cor) da peça (0 para brancas, 1 para pretas)
     */
	public Rei(Posicao posicao, int cor) {
		super(posicao, cor);
	}

	
	@Override
	public String toString() {
	    return "R"+this.getCor();
	}
	
	 /**
     * Verifica se uma posição está sob ataque por peças inimigas.
     * @param posicao (Posição) a ser verificada
     * @param tabuleiro (Tabuleiro) atual do jogo
     * @return true se a posição estiver sob ataque, false caso contrário
     */
	private boolean casaAtacada(Posicao posicao, Tabuleiro tabuleiro) {
	    int corInimiga = (this.cor == 0) ? 1 : 0;
	    ArrayList<Peca> pecasInimigas = tabuleiro.getPecasPorCor(corInimiga);

	    for (Peca peca : pecasInimigas) {
	        if (!(peca instanceof Rei)) { // ignora o rei inimigo
	            if (peca.movValidos(tabuleiro).contains(posicao)) {
	                return true;
	            }
	        }
	    }
	    return false;
	}
    
	/**
     * Atualiza o estado de xeque do rei.
     * @param tabuleiro (Tabuleiro) atual do jogo
     */
    public void verificaXeque(Tabuleiro tabuleiro) {
        this.estaXeque = casaAtacada(this.posicao, tabuleiro);
    }
    
    /**
     * Verifica se o rei está em xeque.
     * @return true se o rei está em xeque, false caso contrário
     */
    public boolean estaEmXeque() {
        return this.estaXeque;
    }

    public boolean RoquePequenoValido(Tabuleiro tabuleiro, Jogador j) {
        if (j.reiMoveu || this.estaXeque) return false;

        int linha = j.getLinhaInicial();  // 7 para C, 0 para P
        Posicao torrePos = new Posicao(linha, 7);  // h1 ou h8

        Peca torre = tabuleiro.getPeca(torrePos);
        if (!(torre instanceof Torre)) return false;
        if (j.torreDireitaMoveu) return false;

        Posicao f = new Posicao(linha, 5);  // f1 ou f8
        Posicao g = new Posicao(linha, 6);  // g1 ou g8
        if (tabuleiro.existePeca(f) || tabuleiro.existePeca(g)) return false;

        if (casaAtacada(f, tabuleiro) || casaAtacada(g, tabuleiro)) return false;

        return true;
    }

    
    public boolean RoqueGrandeValido(Tabuleiro tabuleiro, Jogador j) {
        if (j.reiMoveu || this.estaXeque) return false;

        int linha = j.getLinhaInicial();  // 7 para C, 0 para P
        Posicao torrePos = new Posicao(linha, 0);  // a1 ou a8

        Peca torre = tabuleiro.getPeca(torrePos);
        if (!(torre instanceof Torre)) return false;
        if (j.torreEsquerdaMoveu) return false;

        Posicao b = new Posicao(linha, 1);
        Posicao c = new Posicao(linha, 2);
        Posicao d = new Posicao(linha, 3);
        if (tabuleiro.existePeca(b) || tabuleiro.existePeca(c) || tabuleiro.existePeca(d)) return false;

        if (casaAtacada(c, tabuleiro) || casaAtacada(d, tabuleiro)) return false;

        return true;
    }
		
    
    /**
     * Calcula todos os movimentos válidos para o rei no tabuleiro atual.
     * @param tabuleiro Tabuleiro atual do jogo
     * @return Lista de posições válidas para movimento (1 casa para qualquer direção)
     */
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
	    
		
		 
		this.movimentos = movimentos;
	    return this.movimentos;
	}

	@Override
	public String getTipoPeca() {
		return "R";
	}

    public Peca clonar(Tabuleiro tabuleiro) {
        Rei clone = new Rei(posicao, cor);
        clone.setPosicao(new Posicao(this.posicao.getLinha(), this.posicao.getColuna()));
		ArrayList<Posicao> movimentos = clone.movValidos(tabuleiro);
        clone.setMovimentos(movimentos);
        return clone;
    }

}

