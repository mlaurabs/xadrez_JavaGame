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
	
	public boolean roqueValido = false;
	
	
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

    public boolean RoquePequenoValido(Tabuleiro tabuleiro) {
        if (this.jaMoveu || this.estaXeque) return false; // o rei não pode ter se movido e nem estar em xeque

        int linha = this.posicao.getLinha();
        int coluna = this.posicao.getColuna();

        // Torre do roque pequeno está em h1 ou h8
        Posicao torrePos = (this.cor == 0) ? new Posicao("h8") : new Posicao("h1");
        Peca torre = tabuleiro.getPeca(torrePos);

        if (!(torre instanceof Torre)) return false;
        Torre t = (Torre) torre;
        if (t.getJaMoveu()) return false; // a torre não pode ter se movido

        // Casas entre rei e torre devem estar livres: f1, g1 ou f8, g8
        Posicao f = new Posicao(linha, 5);
        Posicao g = new Posicao(linha, 6);
        if (tabuleiro.existePeca(f) || tabuleiro.existePeca(g)) return false;

        // Rei não pode passar por casas atacadas
        if (casaAtacada(f, tabuleiro) || casaAtacada(g, tabuleiro)) return false;

        return true;
    }

    public boolean RoqueGrandeValido(Tabuleiro tabuleiro) {
        if (this.jaMoveu || this.estaXeque) return false;

        int linha = this.posicao.getLinha();
        int coluna = this.posicao.getColuna();
        System.out.printf(">>>RoqueGrandeValido - linha: %d - coluna: %d\n\n", linha, coluna);

        // Torre do roque grande está em a1 ou a8
        Posicao torrePos = (this.cor == 0) ? new Posicao("a1") : new Posicao("a8");
        Peca torre = tabuleiro.getPeca(torrePos);

        System.out.println("nao eh torre\n");
        if (!(torre instanceof Torre)) return false;
        Torre t = (Torre) torre;
        System.out.println("torre ja se moveu\n");
        if (t.getJaMoveu()) return false;

        // Casas entre rei e torre devem estar livres: b1, c1, d1 ou b8, c8, d8
        Posicao b = new Posicao(linha, 1);
        Posicao c = new Posicao(linha, 2);
        Posicao d = new Posicao(linha, 3);
        System.out.println("existe peca entre o rei e a torre\n");
        if (tabuleiro.existePeca(b) || tabuleiro.existePeca(c) || tabuleiro.existePeca(d)) return false;

        // Rei não pode passar por casas atacadas
        System.out.println("casa atacada\n");
        if (casaAtacada(c, tabuleiro) || casaAtacada(d, tabuleiro)) return false;

        System.out.println("roque grande valido");
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
	    
		
		  if(RoquePequenoValido(tabuleiro)) { 
			  if(this.cor == 0) { 
				  movimentos.add(new Posicao("g8"));
			  }else{ 
				  movimentos.add(new Posicao("g1")); 
		      }
			  this.roqueValido = true;
		  }
		  
		  if(RoqueGrandeValido(tabuleiro)) { 
			  if(this.cor == 0) { 
				  movimentos.add(new Posicao("c8"));
		      } else{ 
		    	  movimentos.add(new Posicao("c1")); 
		     }
			  this.roqueValido = true;
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

