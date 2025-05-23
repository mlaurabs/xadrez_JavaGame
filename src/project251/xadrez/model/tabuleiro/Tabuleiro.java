package project251.xadrez.model.tabuleiro;
import project251.xadrez.model.api.Jogador;
import java.util.ArrayList;
import project251.xadrez.model.figura.*;
import java.util.Scanner;

public class Tabuleiro {

    private static final int LINHAS = 8;
    private static final int COLUNAS = 8;
    private Peca[][] pecas;
    
    public Tabuleiro() {
        pecas = new Peca[LINHAS][COLUNAS];
    }

    public Tabuleiro clonar() {
    Tabuleiro copia = new Tabuleiro();

    for (int linha = 0; linha < 8; linha++) {
        for (int coluna = 0; coluna < 8; coluna++) {
            Posicao pos = new Posicao(linha, coluna);
            Peca original = this.getPeca(pos);
            
            if (original != null) {
                Peca copiaPeca = original.clonar(this); // cada peça deve saber se clonar
                copia.colocarPeca(copiaPeca, new Posicao(linha, coluna));
            }
        }
    }

    return copia;
    }
    
    public void colocarPeca(Peca peca, Posicao posicao) {
        if (existePeca(posicao)) {
            throw new IllegalArgumentException("Já existe uma peça na posição " + posicao);
        }
        pecas[posicao.getLinha()][posicao.getColuna()] = peca;
    }
    
    public void promovePeca(Peca antiga, Peca nova, Posicao posicao) {
    	if (existePeca(posicao) && antiga instanceof Peao) {
    		pecas[posicao.getLinha()][posicao.getColuna()] = nova;
        }
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
    
    public Peca escolherPromocao(Posicao posicao, int cor, Scanner scanner) {
        System.out.println("\nPROMOÇÃO DE PEÃO!");
        System.out.println("Escolha a peça para promover:");
        System.out.println("1 - Dama");
        System.out.println("2 - Torre");
        System.out.println("3 - Bispo");
        System.out.println("4 - Cavalo");
        
        while (true) {
            System.out.print("Opção (1-4): ");
            int opcao = scanner.nextInt();
            scanner.nextLine(); // Limpa o buffer
            
            switch (opcao) {
                case 1: return new Dama(posicao, cor);
                case 2: return new Torre(posicao, cor);
                case 3: return new Bispo(posicao, cor);
                case 4: return new Cavalo(posicao, cor);
                default: System.out.println("Opção inválida!");
            }
        }
    }
    
    public boolean moverPeca(Posicao origem, Posicao destino, Jogador j) {
        Peca peca = getPeca(origem);
        Peca peca_destino = getPeca(destino);
        if (peca == null) {
            throw new IllegalArgumentException("Não há peça na posição de origem: " + origem);
        }

        // Verifica se o movimento é válido
		 if (!peca.movValidos(this).contains(destino)) { throw new
			 IllegalArgumentException("Movimento inválido para a peça em: " + destino); 
		 } else {
		     // Remover peça capturada (se houver)
			    if (existePeca(destino)) {
		            removerPeca(destino);
					j.adicionarPecaCapturada(peca_destino.getTipoPeca());
		        }
		        
		        if (peca instanceof Peao peao) {
		            peao.setJaMoveu(true);
		        }

		        // Mover peça
		        removerPeca(origem);
		        peca.setPosicao(destino);
		        colocarPeca(peca, destino);
		        return true;

		 }
	    
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
