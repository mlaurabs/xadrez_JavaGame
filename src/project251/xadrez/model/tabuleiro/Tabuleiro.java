package project251.xadrez.model.tabuleiro;

import project251.xadrez.model.figura.*;

public class Tabuleiro {

    private static final int LINHAS = 8;
    private static final int COLUNAS = 8;
    private Peca[][] pecas;
    
    public Tabuleiro() {
        pecas = new Peca[LINHAS][COLUNAS];
    }
    
    public void colocarPeca(Peca peca, Posicao posicao) {
        if (existePeca(posicao)) {
            throw new IllegalArgumentException("Já existe uma peça na posição " + posicao);
        }
        pecas[posicao.getLinha()][posicao.getColuna()] = peca;
    }
    
    private void comecaJogo() {
        // Peças brancas
        colocarPeca(new Torre(new Posicao(0, 0)), new Posicao(0, 0));
        colocarPeca(new Cavalo(new Posicao(0, 1)), new Posicao(0, 1));
        colocarPeca(new Bispo(new Posicao(0, 2)), new Posicao(0, 2));
        colocarPeca(new Rainha(new Posicao(0, 3)), new Posicao(0, 3));
        colocarPeca(new Rei(new Posicao(0, 4)), new Posicao(0, 4));
        colocarPeca(new Bispo(new Posicao(0, 5)), new Posicao(0, 5));
        colocarPeca(new Cavalo(new Posicao(0, 6)), new Posicao(0, 6));
        colocarPeca(new Torre(new Posicao(0, 7)), new Posicao(0, 7));
        for (int i = 0; i < 8; i++) {
            colocarPeca(new Peao(new Posicao(1, i)), new Posicao(1, i));
        }

        // Peças pretas
        colocarPeca(new Torre(new Posicao(7, 0)), new Posicao(7, 0));
        colocarPeca(new Cavalo(new Posicao(7, 1)), new Posicao(7, 1));
        colocarPeca(new Bispo(new Posicao(7, 2)), new Posicao(7, 2));
        colocarPeca(new Rainha(new Posicao(7, 3)), new Posicao(7, 3));
        colocarPeca(new Rei(new Posicao(7, 4)), new Posicao(7, 4));
        colocarPeca(new Bispo(new Posicao(7, 5)), new Posicao(7, 5));
        colocarPeca(new Cavalo(new Posicao(7, 6)), new Posicao(7, 6));
        colocarPeca(new Torre(new Posicao(7, 7)), new Posicao(7, 7));
       

    public int getLinhas() {
        return LINHAS;
    }

    public int getColunas() {
        return COLUNAS;
    }

    public Object getPeca(int linha, int coluna) {
        validarPosicao(linha, coluna);
        return pecas[linha][coluna];
    }

    public Object getPeca(Posicao posicao) {
        return getPeca(posicao.getLinha(), posicao.getColuna());
    }

    public Object removerPeca(Posicao posicao) {
        validarPosicao(posicao.getLinha(), posicao.getColuna());
        if (getPeca(posicao) == null) {
            return null;
        }
        Object removida = getPeca(posicao);
        pecas[posicao.getLinha()][posicao.getColuna()] = null;
        return removida;
    }

    private void validarPosicao(int linha, int coluna) { // quando implementarmos a view pode excluir esse metodo
        if (linha < 0 || linha >= LINHAS || coluna < 0 || coluna >= COLUNAS) {
            throw new IllegalArgumentException("Posição fora do tabuleiro: " + linha + "," + coluna);
        }  
    }

    public boolean posicaoExiste(Posicao posicao) {
        int linha = posicao.getLinha();
        int coluna = posicao.getColuna();
        return linha >= 0 && linha < LINHAS && coluna >= 0 && coluna < COLUNAS;
    }

    public boolean existePeca(Posicao posicao) {
        return getPeca(posicao) != null;
    }
}
