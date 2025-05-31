package project251.xadrez.model;

import java.util.ArrayList;

/**
 * Representa a peça Rei no jogo de xadrez.
 * Implementa movimentos especiais como roque e verificação de xeque.
 */
class Rei extends Peca {
	/** Indica se o rei já realizou algum movimento */
	private boolean jaMoveu = false; 
	
	/** Indica se o rei está em xeque */
	private boolean estaXeque = false;
	
	/** Indica se o rei pode sair do xeque */
	public boolean saiXeque = true;
	
	/** Indica se o roque grande (do lado da dama) é possível */
	private boolean podeRoqueGrande = true;
	private boolean podeRoquePeq = true;
	
	/**
     * Cria um novo rei na posição especificada.
     * @param posicao (Posição) inicial do rei no tabuleiro
     * @param cor (Cor) da peça (0 para brancas, 1 para pretas)
     */
	public Rei(Posicao posicao, int cor) {
		super(posicao, cor);
	}

	 /**
     * Verifica se o rei já realizou algum movimento.
     * @return true se o rei já se moveu, false caso contrário
     */
	public boolean getJaMoveu() { //se falso, pode fazer o roque
		return jaMoveu;
	}

	/**
     * Define o estado de movimento do rei.
     * @param jaMoveu = true, se o rei já realizou movimento
     */
	public void setJaMoveu(boolean jaMoveu) {
		this.jaMoveu = jaMoveu;
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

    /**
     * Verifica se o roque pequeno (do lado do rei) é possível.
     * @param tabuleiro (Tabuleiro) atual do jogo
     * @return true se o roque pequeno for possível, false caso contrário
     */
    public boolean verificaRoquePeq(Tabuleiro tabuleiro) { // em processo de implementação
        Posicao[] pos_brancas = {
            new Posicao("f1"), // f1
            new Posicao("g1")  // g1
        };
        
        Posicao[] pos_pretas = {
            new Posicao("f8"), // f1
            new Posicao("g8")  // g1	
        };


        if (jaMoveu == true) {
            this.podeRoquePeq = false;
        } else {
        	if (estaXeque) {
        		this.podeRoquePeq = false;
        	} else {
                if (this.cor == 0) {
                	Peca peca = tabuleiro.getPeca(new Posicao("h1"));
                	if (!tabuleiro.existePeca(new Posicao("h1")) || !(peca instanceof Torre) || peca.cor != this.cor) {
                		this.podeRoquePeq = false;
                	}
                	if (peca instanceof Torre && peca.cor == this.cor && ((Torre) peca).jaMoveu) { //verifica se a torre já moveu para poder rocar
                	    this.podeRoquePeq = false;
                	}
                    if (casaAtacada(pos_brancas[0], tabuleiro) || casaAtacada(pos_brancas[1], tabuleiro)
                    || tabuleiro.existePeca(pos_brancas[0]) || tabuleiro.existePeca(pos_brancas[1])){
                        this.podeRoquePeq = false;
                    }
                } else {
                	Peca peca = tabuleiro.getPeca(new Posicao("h8"));
                	if (!tabuleiro.existePeca(new Posicao("h8")) || !(peca instanceof Torre) || peca.cor != this.cor) {
                		this.podeRoquePeq = false;
                	}
                	if (peca instanceof Torre && peca.cor == this.cor && ((Torre) peca).jaMoveu) {//verifica se a torre já moveu para poder rocar
                	    this.podeRoquePeq = false;
                	}
                    if (casaAtacada(pos_pretas[0], tabuleiro) || casaAtacada(pos_pretas[1], tabuleiro)
                            || tabuleiro.existePeca(pos_pretas[0]) || tabuleiro.existePeca(pos_pretas[1])){
                        this.podeRoquePeq = false;
                    }
                }
        	}
        }
        return this.podeRoquePeq;
    }
    
    // em processo de implementação
    public boolean verificaRoqueGrande(Tabuleiro tabuleiro) {
        Posicao[] pos_brancas = {
                new Posicao("d1"), 
                new Posicao("c1"),
                new Posicao("b1") 
        };
        
        Posicao[] pos_pretas = {
                new Posicao("d8"), 
                new Posicao("c8"),
                new Posicao("b8") 
        };

        if (jaMoveu == true) {
            this.podeRoqueGrande = false;
        } else {
        	if (estaXeque) {
        		this.podeRoqueGrande = false;
        	} else {
                if (this.cor == 0) {
                	Peca peca = tabuleiro.getPeca(new Posicao("a1"));
                	if (!tabuleiro.existePeca(new Posicao("a1")) || !(peca instanceof Torre) || peca.cor != this.cor) {
                		this.podeRoqueGrande = false;
                	}
                	if (peca instanceof Torre && peca.cor == this.cor && ((Torre) peca).jaMoveu) {
                		this.podeRoqueGrande = false;
                	}
                    if (casaAtacada(pos_brancas[0], tabuleiro) || casaAtacada(pos_brancas[1], tabuleiro) || casaAtacada(pos_brancas[2], tabuleiro) 
                    || tabuleiro.existePeca(pos_brancas[0]) || tabuleiro.existePeca(pos_brancas[1]) || tabuleiro.existePeca(pos_brancas[2])) {
                        this.podeRoqueGrande = false;
                    }
                } else {
                	Peca peca = tabuleiro.getPeca(new Posicao("a8"));
                	if (!tabuleiro.existePeca(new Posicao("a8")) || !(peca instanceof Torre) || peca.cor != this.cor) {
                		this.podeRoqueGrande = false;
                	}
                	if (peca instanceof Torre && peca.cor == this.cor && ((Torre) peca).jaMoveu) {
                		this.podeRoqueGrande = false;
                	}
                    if (casaAtacada(pos_pretas[0], tabuleiro) || casaAtacada(pos_pretas[1], tabuleiro) || casaAtacada(pos_pretas[2], tabuleiro) 
                            || tabuleiro.existePeca(pos_pretas[0]) || tabuleiro.existePeca(pos_pretas[1]) || tabuleiro.existePeca(pos_pretas[2])) {
                        this.podeRoqueGrande = false;
                    }
                }
        	}
        }
        return this.podeRoqueGrande;
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
	    
	    if(verificaRoquePeq(tabuleiro)) {
	    	if(this.cor == 0) {
		    	movimentos.add(new Posicao("g1")); 
	    	} else{
	    		movimentos.add(new Posicao("g8")); 
	    	}
	    }
	    
	    if(verificaRoqueGrande(tabuleiro)) {
	    	if(this.cor == 0) {
		    	movimentos.add(new Posicao("c1")); 
	    	} else{
	    		movimentos.add(new Posicao("c8")); 
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

