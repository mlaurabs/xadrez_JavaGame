package project251.xadrez.model.tabuleiro;
import project251.xadrez.model.api.Jogador;
import java.util.ArrayList;
import project251.xadrez.model.figura.*;
import java.util.Scanner;

/**
 * Representa o tabuleiro de xadrez e suas operações básicas.
 * Gerencia a posição das peças, movimentos válidos e estado do jogo.
 */
public class Tabuleiro {

    private static final int LINHAS = 8;
    private static final int COLUNAS = 8;
    private Peca[][] pecas;
    
    /**
     * Constrói um tabuleiro vazio de xadrez.
     */
    public Tabuleiro() {
        pecas = new Peca[LINHAS][COLUNAS];
    }

    public int getLinhas() {
        return LINHAS;
    }

    public int getColunas() {
        return COLUNAS;
    }
    
    /**
     * Cria uma cópia deste tabuleiro.
     * @return Novo objeto Tabuleiro com cópias de todas as peças
     */
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
    
    /**
     * Coloca uma peça em determinada posição do tabuleiro.
     * @param peca (Peça) a ser posicionada
     * @param posicao (Posicao) com coordenadas (linha, coluna)
     * @throws IllegalArgumentException Se a posição já estiver ocupada
     */
    public void colocarPeca(Peca peca, Posicao posicao) {
        if (existePeca(posicao)) {
            throw new IllegalArgumentException("Já existe uma peça na posição " + posicao);
        }
        pecas[posicao.getLinha()][posicao.getColuna()] = peca;
    }
    
    /**
     * Promove um peão para outra peça.
     * @param antig peão (Peão) a ser substituído
     * @param nova peça (Peça) que substituirá o peão
     * @param posicao (Posição) onde ocorre a promoção
     */
    public void promovePeca(Peca antiga, Peca nova, Posicao posicao) {
    	if (existePeca(posicao) && antiga instanceof Peao) {
    		pecas[posicao.getLinha()][posicao.getColuna()] = nova;
        }
    }
    
    /**
     * Inicializa o tabuleiro com as peças nas posições iniciais padrão.
     */
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
    
    /**
     * Exibe o tabuleiro atual no console.
     * Mostra a representação visual com coordenadas.
     */
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

 
    /**
     * Obtém a peça em determinada posição.
     * @param linha Índice da linha (0-7)
     * @param coluna Índice da coluna (0-7)
     * @return Peça na posição ou null se vazio
     */
    public Peca getPeca_interno(int linha, int coluna) {
        if(validarPosicao(linha, coluna)) {
        	return pecas[linha][coluna];
        }
        return null;
    }

    /**
     * Obtém a peça em determinada posição.
     * @param posicao (Posicao) com notacão no formato ex.:"e4"
     * @return Peça na posição ou null se vazio
     */
    public Peca getPeca(Posicao posicao) {
        return getPeca_interno(posicao.getLinha(), posicao.getColuna());
    }

    /**
     * Remove a peça de determinada posição.
     * @param posicao (Posição) da peça a ser removida
     * @return Peça removida ou null se posição vazia
     */
    public Object removerPeca(Posicao posicao) {
        if (validarPosicao(posicao.getLinha(), posicao.getColuna())) {
        	if (getPeca(posicao) == null) {
                return null;
            }
            Object removida = getPeca(posicao);
            pecas[posicao.getLinha()][posicao.getColuna()] = null;
            return removida;
        }
        return null;
    }

    /**
     * Valida se as coordenadas estão dentro dos limites do tabuleiro.
     * @param linha Índice da linha
     * @param coluna Índice da coluna
     * @return true se posição válida, false caso contrário
     */
    private boolean validarPosicao(int linha, int coluna) { // quando implementarmos a view pode excluir esse metodo
        if (linha < 0 || linha >= LINHAS || coluna < 0 || coluna >= COLUNAS) {
        	System.out.println("\nPosição fora do tabuleiro: " + linha + "," + coluna);
        	return false;
        }  
        return true;
    }
    
    /**
     * Verifica se uma posição existe no tabuleiro.
     * @param posicao (Posição) a ser verificada
     * @return true se posição válida, false caso contrário
     */
    public boolean posicaoExiste(Posicao posicao) {
        int linha = posicao.getLinha();
        int coluna = posicao.getColuna();
        return linha >= 0 && linha < LINHAS && coluna >= 0 && coluna < COLUNAS;
    }

    /**
     * Verifica se existe uma peça em determinada posição.
     * @param posicao Posição a ser verificada
     * @return true se existir peça, false caso contrário
     */
    public boolean existePeca(Posicao posicao) {
        return getPeca(posicao) != null;
    }
    
    
    /**
     * Interface para promoção de peão.
     * @param posicao (Posição) onde ocorre a promoção
     * @param cor (Cor) da peça (0 para branco, 1 para preto)
     * @param scanner (Scanner) para entrada do usuário
     * @return Nova peça escolhida para promoção
     */
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
    
    
    /**
     * Move uma peça de uma posição para outra.
     * @param origem (Posição) de origem da peça
     * @param destino (Posição) de destino da peça
     * @param jogador (Jogador) que está realizando o movimento
     * @return true se movimento foi realizado, false caso contrário
     */
    public boolean moverPeca(Posicao origem, Posicao destino, Jogador j) {
        Peca peca = getPeca(origem);
        Peca peca_destino = getPeca(destino);
        if (peca == null) {
        	System.out.println("\nNão há peça na posição de origem: " + origem);
        	return false;
        }

        // Verifica se o movimento é válido
		 if (!peca.movValidos(this).contains(destino)) {
			 System.out.println("\nMovimento inválido para a peça em: " + destino);
			 return false;
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

    
    /**
     * Obtém todas as peças de uma determinada cor.
     * @param cor (Cor) das peças a serem recuperadas (0 para branco, 1 para preto)
     * @return Lista de peças da cor especificada
     */
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
