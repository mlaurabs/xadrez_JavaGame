package project251.xadrez.model.figura;

import java.util.ArrayList;

import project251.xadrez.model.tabuleiro.Posicao;
import project251.xadrez.model.tabuleiro.Tabuleiro;

public class Dama extends Peca {

    public Dama(Posicao posicao, int cor) {
        super(posicao, cor);
    }

    @Override
    public String toString() {
        return "D" + this.getCor();
    }

    @Override
    public ArrayList<Posicao> movValidos(Tabuleiro tabuleiro) {
        ArrayList<Posicao> movimentos = new ArrayList<>();

        // Todas as 8 direções possíveis da dama: diagonais, horizontais e verticais
        int[][] direcoes = {
            {-1, -1}, {-1, 0}, {-1, 1},  // diagonal sup-esq, cima, diagonal sup-dir
            {0, -1},           {0, 1},   // esquerda,       , direita
            {1, -1},  {1, 0},  {1, 1}    // diagonal inf-esq, baixo, diagonal inf-dir
        };

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

    public Peca clonar(Tabuleiro tabuleiro) {
        Dama clone = new Dama(posicao, cor);
        clone.setPosicao(new Posicao(this.posicao.getLinha(), this.posicao.getColuna()));
        ArrayList<Posicao> movimentos = clone.movValidos(tabuleiro);
        clone.setMovimentos(movimentos);
        return clone;
    }
}