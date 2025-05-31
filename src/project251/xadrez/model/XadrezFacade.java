package project251.xadrez.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


/**
 * Classe principal que controla o fluxo do jogo de xadrez.
 * Implementa o padrão Singleton para garantir uma única instância.
 * Responsável por gerenciar turnos, movimentos e regras do jogo.
 */
public class XadrezFacade {
    private final Tabuleiro tabuleiro;
    private Jogador jogadorAtual;
    private final Scanner scanner;

 // 1. Construtor privado
    private XadrezFacade() {
        this.tabuleiro = new Tabuleiro();
        this.tabuleiro.comecaJogo();
        this.jogadorAtual = Jogador.C;
        this.scanner = new Scanner(System.in);
    }

    // 2. Instância única estática
    private static XadrezFacade instance;

    // 3. Método público de acesso
    public static synchronized XadrezFacade getInstance() {
        if (instance == null) {
            instance = new XadrezFacade();
        }
        return instance;
    }

    /**
     * Inicia o loop principal do jogo.
     * Exibe a introdução e processa turnos alternados.
     */
    //public void iniciarJogo() {
      
    //}


    //System.out.println("\n===== TURNO DO JOGADOR " + jogadorAtual.getNome() + " =====");

    //verificarXeque();

	/*
	 * if (jogadorAtual.emXeque && jogadorAtual.xeque_mate) {
	 * System.out.println(">>> XEQUE-MATE! Jogador " +
	 * jogadorAtual.getNome().toUpperCase() + " perdeu o jogo. <<<");
	 * System.exit(0); }
	 * 
	 * tabuleiro.exibirTabuleiro();
	 * 
	 * Posicao origem =
	 * obterPosicao("\nDigite a posição da peça que deseja mover (ex: e2): "); if
	 * (origem == null) return;
	 * 
	 * Peca peca = tabuleiro.getPeca(origem); if (!validarPecaSelecionada(peca))
	 * return;
	 * 
	 * ArrayList<Posicao> movimentosValidos = obterMovimentosValidos(peca); if
	 * (movimentosValidos.isEmpty()) {
	 * System.out.println("\nEssa peça não tem movimentos válidos."); return; }
	 * 
	 * exibirMovimentos(movimentosValidos);
	 * 
	 * Posicao destino = obterDestino(movimentosValidos); if (destino == null)
	 * return;
	 * 
	 * tabuleiro.moverPeca(origem, destino, jogadorAtual);
	 * 
	 * // Verifica promoção de peão if (peca instanceof Peao && (destino.getLinha()
	 * == 0 || destino.getLinha() == 7)) {
	 * System.out.println("\n***** PEÃO ANTES DA PROMOÇÃO ***** \n");
	 * tabuleiro.exibirTabuleiro(); Peca novaPeca =
	 * tabuleiro.escolherPromocao(destino, peca.cor, scanner);
	 * tabuleiro.promovePeca(peca, novaPeca, destino); }
	 * 
	 * verificarXequeAdversario(); Jogador.imprimirPlacarFormatado(); jogadorAtual =
	 * jogadorAtual.proximo();
	 */
    
    public ArrayList<Posicao> processaTurno(Jogador j, Posicao origem) {
    	//System.out.println("\n===== TURNO DO JOGADOR " + j.getNome() + " =====");
    	
        if (origem == null) return null;
        
        Peca peca = tabuleiro.getPeca(origem);
        System.out.printf("Tipo da peça: %s\n",peca.getTipoPeca());
        System.out.printf("Cor da peça: %s\n", peca.getCor());
        if (!validarPecaSelecionada(peca)) return null;
        
        ArrayList<Posicao> movimentosValidos = obterMovimentosValidos(peca);
        if (movimentosValidos.isEmpty()) {
        	System.out.println("\nEssa peça não tem movimentos válidos.");
            return null;
        }

        exibirMovimentos(movimentosValidos);

    	return movimentosValidos;
    }
    
    /**
     * Verifica se o rei do jogador atual está em xeque.
     */
    private void verificarXeque() {
        jogadorAtual.emXeque = false;
        for (Peca p : tabuleiro.getPecasPorCor(jogadorAtual.getCor())) {
            if (p instanceof Rei rei) {
                rei.verificaXeque(tabuleiro);
                if (rei.estaEmXeque()) {
                    jogadorAtual.emXeque = true;
                    existeMovimentoQueTiraReiDoXeque();
                    System.out.println(">>> SEU REI ESTÁ EM XEQUE! <<<\n");
                }
            }
        }
    }

    /**
     * Verifica se o movimento atual colocou o adversário em xeque.
     */
    private void verificarXequeAdversario() {
        Jogador adversario = jogadorAtual.proximo();
        for (Peca p : tabuleiro.getPecasPorCor(adversario.getCor())) {
            if (p instanceof Rei rei) {
                rei.verificaXeque(tabuleiro);
                if (rei.estaEmXeque()) {
                    System.out.println("\n>>> O rei das peças " + adversario.getNome().toUpperCase() + " está em XEQUE! <<<");
                }
            }
        }
    }

    /**
     * Verifica se existe algum movimento que tire o rei do xeque.
     * Atualiza o estado de xeque-mate se necessário.
     */
    private void existeMovimentoQueTiraReiDoXeque() {
        jogadorAtual.xeque_mate = true; 
        Tabuleiro tabuleiroAux = tabuleiro.clonar(); 

        // Pega peças do jogador na cópia
        ArrayList<Peca> pecas = tabuleiro.getPecasPorCor(jogadorAtual == Jogador.P ? 0 : 1);

        for (Peca peca : pecas) {
            Posicao origem = peca.getPosicao();
            ArrayList<Posicao> movimentos = peca.movimentos;

            for (Posicao destino : movimentos) {
                // Clonar novamente o tabuleiro antes de cada teste (para isolar os efeitos)
                Tabuleiro simulado = tabuleiroAux.clonar();

                simulado.moverPeca(origem,destino, jogadorAtual); 

                // Verifica se o rei ainda está em xeque após o movimento
                for (Peca outra : simulado.getPecasPorCor(jogadorAtual.getCor())) {
                    if (outra instanceof Rei rei) {
                        rei.verificaXeque(simulado);
                        if (rei.estaEmXeque()) {
                            ArrayList<Posicao> vazio = new ArrayList<>();
                            peca.setMovimentos(vazio);
                        } else {
                        	jogadorAtual.xeque_mate = false;
                            ArrayList<Posicao> new_movimentos = new ArrayList<>();
                            new_movimentos.add(destino);
                            peca.setMovimentos(new_movimentos);
                        }
                    }
                }
            }
        }
    }


    /**
     * Obtém uma posição válida do usuário.
     * @param mensagem (String) a ser exibida para o usuário
     * @return Objeto (Posicao) ou null se inválido
     */
    private Posicao obterPosicao(String mensagem) {
        System.out.print(mensagem);
        String entrada = scanner.nextLine();

        if ("sair".equalsIgnoreCase(entrada)) {
            System.out.println("\nJogo Encerrado!");
            System.exit(0);
        }

        try {
            return new Posicao(entrada);
        } catch (IllegalArgumentException e) {
            System.out.println("\nPosição inválida.");
            return null;
        }
    }
    

    /**
     * Valida se a peça selecionada pode ser movida pelo jogador atual.
     * @param peca (Peça) a ser validada
     * @return true se a peça for válida, false caso contrário
     */
    private boolean validarPecaSelecionada(Peca peca) {
        if (peca == null) {
            System.out.println("\nNão há peça nessa posição.");
            return false;
        }
        if (!peca.getCor().toString().equalsIgnoreCase(jogadorAtual.toString())) {
            System.out.println("\nVocê só pode mover suas próprias peças!");
            return false;
        }
        return true;
    }

    /**
     * Obtém os movimentos válidos para uma peça.
     * @param peca (Peça) a ser analisada
     * @return Lista de posições válidas
     */
    public ArrayList<Posicao> obterMovimentosValidos(Peca peca) {
        if (!jogadorAtual.emXeque) {
            peca.movValidos(tabuleiro);
        }
        return new ArrayList<>(peca.movimentos);
    }


    /**
     * Exibe os movimentos válidos para o usuário.
     * @param movimentos Lista de posições válidas
     */
    private void exibirMovimentos(ArrayList<Posicao> movimentos) {
        System.out.println("\nMovimentos válidos:");
        for (Posicao destino : movimentos) {
            System.out.print(destino + " ");
        }
        System.out.println();
    }


    /**
     * Obtém uma posição de destino válida do usuário.
     * @param movimentosValidos Lista de movimentos permitidos
     * @return Posição de destino selecionada
     */
    private Posicao obterDestino(ArrayList<Posicao> movimentosValidos) {
        while (true) {
            System.out.print("\nDigite a posição de destino (ex: e4): ");
            String destinoStr = scanner.nextLine();
            try {
                Posicao destino = new Posicao(destinoStr);
                if (movimentosValidos.contains(destino)) {
                    return destino;
                }
                System.out.println("Movimento inválido.");
            } catch (IllegalArgumentException e) {
                System.out.println("Posição de destino inválida.");
            }
        }
    }

	public Tabuleiro getTabuleiro() {
		return tabuleiro;
	}
}
