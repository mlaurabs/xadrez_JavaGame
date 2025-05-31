package project251.xadrez.model;

import java.util.ArrayList;


class Dama extends Peca {
	/**
	 * Representa a peça Dama no jogo de xadrez.
	 * Controla movimentos válidos, estado de movimento e clonagem.
	 */
    public Dama(Posicao posicao, int cor) {
        super(posicao, cor);
    }

    @Override
    public String toString() {
        return "D" + this.getCor();
    }
    
	// para cada movimento, temos que verificar os movimentos possiveis
    @Override
    public ArrayList<Posicao> movValidos(Tabuleiro tabuleiro) {
        ArrayList<Posicao> movimentos = new ArrayList<>();

        // Todas as 8 direções possíveis da dama: diagonais, horizontais e verticais
        int[][] direcoes = {
            {-1, -1}, {-1, 0}, {-1, 1},  // diagonal sup-esq, cima, diagonal sup-dir
            {0, -1},           {0, 1},   // esquerda,       , direita
            {1, -1},  {1, 0},  {1, 1}    // diagonal inf-esq, baixo, diagonal inf-dir
        };
        
        /**
         * Para cada direção, ele verifica se não há nenhuma peça atrapalhando.
         * Se for uma peça da sua cor, ele para e segue para verificar outra direção.
         * Se for uma peça de outra cor, ele adiciona a captura e depois para.
         */
        
        for (int[] direcao : direcoes) {
            int novaLinha = posicao.getLinha() + direcao[0];
            int novaColuna = posicao.getColuna() + direcao[1];

            while (tabuleiro.posicaoExiste(new Posicao(novaLinha, novaColuna))) {
                Posicao novaPos = new Posicao(novaLinha, novaColuna);

                if (!tabuleiro.existePeca(novaPos)) {
                    movimentos.add(novaPos);
                } else {
                    if (this.cor != tabuleiro.getPeca(novaPos).cor) {
                        movimentos.add(novaPos); // Pode capturar inimigo
                    }
                    break; // Encerra o caminho nessa direção
                }

                novaLinha += direcao[0];
                novaColuna += direcao[1];
            }
        }
        this.movimentos = movimentos;
        return movimentos;
    }

    @Override
    public String getTipoPeca() {
        return "D";
    }
    
    
    // o mesmo das outras peças
    public Peca clonar(Tabuleiro tabuleiro) {
        Dama clone = new Dama(posicao, cor);
        clone.setPosicao(new Posicao(this.posicao.getLinha(), this.posicao.getColuna()));
        ArrayList<Posicao> movimentos = clone.movValidos(tabuleiro);
        clone.setMovimentos(movimentos);
        return clone;
    }
}