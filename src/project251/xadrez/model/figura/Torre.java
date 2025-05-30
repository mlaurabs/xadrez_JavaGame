package project251.xadrez.model.figura;

import java.util.ArrayList;

import project251.xadrez.model.tabuleiro.Posicao;
import project251.xadrez.model.tabuleiro.Tabuleiro;

/**
 * Representa a peça Torre no jogo de xadrez.
 * Controla movimentos válidos, estado de movimento (para roque) e clonagem.
 */
public class Torre extends Peca {
	public boolean jaMoveu = false; 

	/**
     * Cria uma nova torre na posição especificada.
     * @param posicao (Posição) inicial da torre no tabuleiro
     * @param cor (Cor) da peça (0 para branca, 1 para preta)
     */
	public Torre(Posicao posicao, int cor) {
		super(posicao, cor);
	}

    /**
     * Verifica se a torre já realizou algum movimento.
     * @return true se a torre já se moveu
     */

	public boolean getJaMoveu() { //se falso, pode fazer o roque
		return jaMoveu;
	}

	  /**
     * Define o estado de movimento da torre.
     * @param jaMoveu true se a torre já se moveu, false caso contrário
     */
	public void setJaMoveu(boolean jaMoveu) {
		this.jaMoveu = jaMoveu;
	}
	
	/**
     * Retorna a representação textual da torre.
     * @return String no formato "T" seguido da cor (ex: "T0" para torre branca)
     */
	@Override
	public String toString() {
	    return "T"+this.getCor();
	}
	
	  /**
     * Calcula todos os movimentos válidos para a torre no tabuleiro atual.
     * A torre move-se em linhas retas (horizontal/vertical) sem pular outras peças.
     * @param tabuleiro Tabuleiro atual do jogo
     * @return Lista de posições válidas para movimento
     */
	@Override
	public ArrayList<Posicao> movValidos(Tabuleiro tabuleiro) {
		ArrayList<Posicao> movimentos = new ArrayList<>();
        int linha = posicao.getLinha();
        int coluna = posicao.getColuna();
        int mov_cor;

     // Determina a direção do movimento com base na cor da peça
     if (this.cor == 0) { // se as peças forem brancas elas andam para frente
         mov_cor = 1;
     } else {
         mov_cor = -1; // se as peças forem pretas, elas andam para trás
     }

     // Diagonal superior direita (linha + i, coluna + i)
     int i = 1;
     Posicao frente;
     do {
         if (!tabuleiro.posicaoExiste(new Posicao(linha + i * mov_cor, coluna))) {
             break;  // Sai do loop se a posição não existir
         }
         frente = new Posicao(linha + i * mov_cor, coluna);  // Atualiza a posição
         if (!tabuleiro.existePeca(frente)) {
             movimentos.add(frente);  // Adiciona movimento se não houver peça
         } else {
        	 if (this.cor != tabuleiro.getPeca(frente).cor) {
            	 movimentos.add(frente); 
        	 }
        	 break;
         }
         i++;
     } while (!tabuleiro.existePeca(frente) && tabuleiro.posicaoExiste(frente));

     // Diagonal inferior direita (linha - i, coluna + i)
     i = 1;
     Posicao tras;
     do {
         if (!tabuleiro.posicaoExiste(new Posicao(linha - i * mov_cor, coluna))) {
             break;  // Sai do loop se a posição não existir
         }
         tras = new Posicao(linha - i * mov_cor, coluna);  // Atualiza a posição
         if (!tabuleiro.existePeca(tras)) {
             movimentos.add(tras);  // Adiciona movimento se não houver peça
         } else {
        	 if (this.cor != tabuleiro.getPeca(tras).cor) {
            	 movimentos.add(tras); 
        	 }
        	 break;
         }
         i++;
     } while (!tabuleiro.existePeca(tras) && tabuleiro.posicaoExiste(tras));

     // Diagonal superior esquerda (linha + i, coluna - i)
     i = 1;
     Posicao esq;
     do {
         if (!tabuleiro.posicaoExiste(new Posicao(linha, coluna - i * mov_cor))) {
             break;  // Sai do loop se a posição não existir
         }
         esq = new Posicao(linha, coluna - i * mov_cor);  // Atualiza a posição
         if (!tabuleiro.existePeca(esq)) {
             movimentos.add(esq);  // Adiciona movimento se não houver peça
         } else {
        	 if (this.cor != tabuleiro.getPeca(esq).cor) {
            	 movimentos.add(esq); 
        	 }
        	 break;
         }
         i++;
     } while (!tabuleiro.existePeca(esq) && tabuleiro.posicaoExiste(esq));

     // Diagonal inferior esquerda (linha - i, coluna - i)
     i = 1;
     Posicao dir;
     do {
         if (!tabuleiro.posicaoExiste(new Posicao(linha, coluna + i * mov_cor))) {
             break;  // Sai do loop se a posição não existir
         }
         dir = new Posicao(linha, coluna + i * mov_cor);  // Atualiza a posição
         if (!tabuleiro.existePeca(dir)) {
             movimentos.add(dir);  // Adiciona movimento se não houver peça
         } else {
        	 if (this.cor != tabuleiro.getPeca(dir).cor) {
            	 movimentos.add(dir); 
        	 }
        	 break;
         }
         i++;
     } while (!tabuleiro.existePeca(dir) && tabuleiro.posicaoExiste(dir));

        this.movimentos = movimentos;
		return movimentos;
	}

	/**
     * Retorna o tipo da peça.
     * @return "T" (identificador de torre)
     */
	@Override
	public String getTipoPeca() {
		return "T";
	}

	/**
     * Cria uma cópia exata desta torre.
     * @param tabuleiro Tabuleiro de referência para cálculo de movimentos
     * @return Nova instância de Torre com mesmo estado
     */
    public Peca clonar(Tabuleiro tabuleiro) {
        Torre clone = new Torre(posicao, cor);
        clone.setPosicao(new Posicao(this.posicao.getLinha(), this.posicao.getColuna()));
        ArrayList<Posicao> movimentos = clone.movValidos(tabuleiro);
        clone.setMovimentos(movimentos);
        return clone;
    }
}
