package project251.xadrez.model;

import java.util.ArrayList;

class Bispo extends Peca {

	public Bispo(Posicao posicao, int cor) {
		super(posicao, cor);
	}
	
	@Override
	public String toString() {
	    return "B"+this.getCor();
	}
	
	// para cada movimento, temos que verificar os movimentos possiveis
	@Override
	public ArrayList<Posicao> movValidos(Tabuleiro tabuleiro) {
		ArrayList<Posicao> movimentos = new ArrayList<>();
        int linha = posicao.getLinha();
        int coluna = posicao.getColuna();
        int mov_cor;

     // Determina a direção do movimento com base na cor da peça
     if (this.cor == 0) { // esse trecho do codigo deixa 'simetrico' os movimentos
         mov_cor = 1;
     } else {
         mov_cor = -1; 
     }

     // Diagonal superior direita (linha + i, coluna + i)
     int i = 1;
     Posicao diagonal_dir;
     do {
         if (!tabuleiro.posicaoExiste(new Posicao(linha + i * mov_cor, coluna + i))) {
             break;  // Sai do loop se a posição não existir
         }
         diagonal_dir = new Posicao(linha + i * mov_cor, coluna + i);  // Atualiza a posição
         if (!tabuleiro.existePeca(diagonal_dir)) {
             movimentos.add(diagonal_dir);  // Adiciona movimento se não houver peça
         } else {
        	 if (this.cor != tabuleiro.getPeca(diagonal_dir).cor) {
            	 movimentos.add(diagonal_dir); 
        	 }
        	 break;
         }
         i++;
     } while (!tabuleiro.existePeca(diagonal_dir) && tabuleiro.posicaoExiste(diagonal_dir));

     // Diagonal inferior direita (linha - i, coluna + i)
     i = 1;
     Posicao diagonal_dir_inf;
     do {
         if (!tabuleiro.posicaoExiste(new Posicao(linha - i * mov_cor, coluna + i))) {
             break;  // Sai do loop se a posição não existir
         }
         diagonal_dir_inf = new Posicao(linha - i * mov_cor, coluna + i);  // Atualiza a posição
         if (!tabuleiro.existePeca(diagonal_dir_inf)) {
             movimentos.add(diagonal_dir_inf);  // Adiciona movimento se não houver peça
         }else {
        	 if (this.cor != tabuleiro.getPeca(diagonal_dir_inf).cor) {
            	 movimentos.add(diagonal_dir_inf); 
        	 }
        	 break;
         }
         i++;
     } while (!tabuleiro.existePeca(diagonal_dir_inf) && tabuleiro.posicaoExiste(diagonal_dir_inf));

     // Diagonal superior esquerda (linha + i, coluna - i)
     i = 1;
     Posicao diagonal_esq;
     do {
         if (!tabuleiro.posicaoExiste(new Posicao(linha + i * mov_cor, coluna - i))) {
             break;  // Sai do loop se a posição não existir
         }
         diagonal_esq = new Posicao(linha + i * mov_cor, coluna - i);  // Atualiza a posição
         if (!tabuleiro.existePeca(diagonal_esq)) {
             movimentos.add(diagonal_esq);  // Adiciona movimento se não houver peça
         } else {
        	 if (this.cor != tabuleiro.getPeca(diagonal_esq).cor) {
            	 movimentos.add(diagonal_esq); 
        	 }
        	 break;
         }
         i++;
     } while (!tabuleiro.existePeca(diagonal_esq) && tabuleiro.posicaoExiste(diagonal_esq));

     // Diagonal inferior esquerda (linha - i, coluna - i)
     i = 1;
     Posicao diagonal_esq_inf;
     do {
         if (!tabuleiro.posicaoExiste(new Posicao(linha - i * mov_cor, coluna - i))) {
             break;  // Sai do loop se a posição não existir
         }
         diagonal_esq_inf = new Posicao(linha - i * mov_cor, coluna - i);  // Atualiza a posição
         if (!tabuleiro.existePeca(diagonal_esq_inf)) {
             movimentos.add(diagonal_esq_inf);  // Adiciona movimento se não houver peça
         } else {
        	 if (this.cor != tabuleiro.getPeca(diagonal_esq_inf).cor) {
            	 movimentos.add(diagonal_esq_inf); 
        	 }
        	 break;
         }
         i++;
     } while (!tabuleiro.existePeca(diagonal_esq_inf) && tabuleiro.posicaoExiste(diagonal_esq_inf));
        this.movimentos = movimentos;
		return movimentos;
	}

	@Override
	public String getTipoPeca() {
		return "B";
	}
	
	// o mesmo das outras classes
    public Peca clonar(Tabuleiro tabuleiro) {
        Bispo clone = new Bispo(posicao, cor);
        clone.setPosicao(new Posicao(this.posicao.getLinha(), this.posicao.getColuna()));
        ArrayList<Posicao> movimentos = clone.movValidos(tabuleiro);
        clone.setMovimentos(movimentos);
        return clone;
    }
	
}
