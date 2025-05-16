package project251.xadrez.model.figura;

import java.util.ArrayList;

import project251.xadrez.model.tabuleiro.Posicao;
import project251.xadrez.model.tabuleiro.Tabuleiro;

public class Rei extends Peca {
	private boolean jaMoveu = false; 
	private boolean estaXeque = false;
	private boolean podeRoqueGrande = true;
	private boolean podeRoquePeq = true;
	
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
    
    public void verificaXeque(Tabuleiro tabuleiro) {
        this.estaXeque = casaAtacada(this.posicao, tabuleiro);
    }

    public boolean verificaRoquePeq(Tabuleiro tabuleiro) {
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

	    return movimentos;
	}

}

