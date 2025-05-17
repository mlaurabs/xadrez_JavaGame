package project251.xadrez.model.tabuleiro;
import project251.xadrez.model.api.Jogador;
import java.util.ArrayList;
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
    
    public void comecaJogo() {
        // Peças brancas
        colocarPeca(new Torre(new Posicao("a1"), 0), new Posicao("a1"));
        colocarPeca(new Cavalo(new Posicao("b1"), 0), new Posicao("b1"));
        colocarPeca(new Bispo(new Posicao("c1"), 0), new Posicao("c1"));
        colocarPeca(new Dama(new Posicao("d1"), 0), new Posicao("d1"));
        colocarPeca(new Rei(new Posicao("e1"), 0), new Posicao("e1"));
        colocarPeca(new Bispo(new Posicao("f1"), 0), new Posicao("f1"));
        colocarPeca(new Cavalo(new Posicao("g1"), 0), new Posicao("g1"));
        colocarPeca(new Torre(new Posicao("h1"), 0), new Posicao("h1"));
        for (char c = 'a'; c <= 'h'; c++) {
            String notacao = "" + c + "2";
            colocarPeca(new Peao(new Posicao(notacao), 0), new Posicao(notacao));
        }

        // Peças pretas
        colocarPeca(new Torre(new Posicao("a8"), 1), new Posicao("a8"));
        colocarPeca(new Cavalo(new Posicao("b8"), 1), new Posicao("b8"));
        colocarPeca(new Bispo(new Posicao("c8"), 1), new Posicao("c8"));
        colocarPeca(new Dama(new Posicao("d8"), 1), new Posicao("d8"));
        colocarPeca(new Rei(new Posicao("e8"), 1), new Posicao("e8"));
        colocarPeca(new Bispo(new Posicao("f8"), 1), new Posicao("f8"));
        colocarPeca(new Cavalo(new Posicao("g8"), 1), new Posicao("g8"));
        colocarPeca(new Torre(new Posicao("h8"), 1), new Posicao("h8"));
        for (char c = 'a'; c <= 'h'; c++) {
            String notacao = "" + c + "7";
            colocarPeca(new Peao(new Posicao(notacao), 1), new Posicao(notacao));
        }
    }
    
    public void exibirTabuleiro() {
        for (int linha = LINHAS - 1; linha >= 0; linha--) {
            System.out.print((linha + 1) + " ");
            for (int coluna = 0; coluna < COLUNAS; coluna++) {
                Peca peca = pecas[linha][coluna];
                if (peca == null) {
                    System.out.print("-- ");
                } else {
                    System.out.print(peca + " ");
                }
            }
            System.out.println();
        }
        System.out.println("   a  b  c  d  e  f  g  h");
    }

    public int getLinhas() {
        return LINHAS;
    }

    public int getColunas() {
        return COLUNAS;
    }

    public Peca getPeca(int linha, int coluna) {
        validarPosicao(linha, coluna);
        return pecas[linha][coluna];
    }

    public Peca getPeca(Posicao posicao) {
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
    
    public void moverPeca(Posicao origem, Posicao destino, Jogador j) {
        Peca peca = getPeca(origem);
        Peca peca_destino = getPeca(destino);
        if (peca == null) {
            throw new IllegalArgumentException("Não há peça na posição de origem: " + origem);
        }

        // Verifica se o movimento é válido
		 if (!peca.movValidos(this).contains(destino)) { throw new
			 IllegalArgumentException("Movimento inválido para a peça em: " + destino); }
		 
        // Remover peça capturada (se houver)
        if (existePeca(destino)) {
            removerPeca(destino);
			/* j.adicionarPecaCapturada(peca_destino.getTipoPeca()); */
        }

        // Mover peça
        removerPeca(origem);
        peca.setPosicao(destino);
        colocarPeca(peca, destino);
        //System.out.println(j.getPecasCapturadasString());
    }

    
    public ArrayList<Peca> getPecasPorCor(int cor) {
        ArrayList<Peca> pecasCor = new ArrayList<>();
        for (int linha = 0; linha < LINHAS; linha++) {
            for (int coluna = 0; coluna < COLUNAS; coluna++) {
                Peca peca = pecas[linha][coluna];
                if (peca != null && peca.cor == cor) {
                    pecasCor.add(peca);
                }
            }
        }
        return pecasCor;
    }
}
