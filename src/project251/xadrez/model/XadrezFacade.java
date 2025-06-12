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
    private ArrayList<Posicao> movimentosValidos = new ArrayList<>();
	private List<TabuleiroObserver> observers = new ArrayList<>();
	
	public Tabuleiro getTabuleiro() {
		return tabuleiro;
	}

 // 1. Construtor privado
    private XadrezFacade() {
        this.tabuleiro = new Tabuleiro();
        this.tabuleiro.comecaJogo();
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

    public void addObserver(TabuleiroObserver observer) {
        observers.add(observer);
    }
    
    public void removeObserver(TabuleiroObserver observer) {
        observers.remove(observer);
    }
    
    public void notificarObservers() {
        for (TabuleiroObserver observer : observers) {
        	observer.atualizar();
        	System.out.println("Notifiquei");
        	
        }
    }
    

    public boolean estaEmXeque(Jogador jogador) {
        for (Peca p : tabuleiro.getPecasPorCor(jogador.getCor())) {
            if (p instanceof Rei rei) {
                rei.verificaXeque(tabuleiro);
                return rei.estaEmXeque();
            }
        }
        return false;
    }
    
    public boolean ehXequeMate(Jogador jogador) {
        jogador.xeque_mate = true;
        Tabuleiro tabuleiroAux = tabuleiro.clonar();
        ArrayList<Peca> pecas = tabuleiro.getPecasPorCor(jogador.getCor());

        for (Peca peca : pecas) {
            Posicao origem = peca.getPosicao();
            peca.movValidos(tabuleiro); // Gera movimentos atuais
            for (Posicao destino : peca.movimentos) {
                Tabuleiro simulado = tabuleiroAux.clonar();
                simulado.moverPeca(origem, destino, jogador);

                for (Peca outra : simulado.getPecasPorCor(jogador.getCor())) {
                    if (outra instanceof Rei rei) {
                        rei.verificaXeque(simulado);
                        if (!rei.estaEmXeque()) {
                            return false; // Existe pelo menos um movimento que salva o rei
                        }
                    }
                }
            }
        }
        return true;
    }
    
    public boolean simulaJogadaRetiraXeque(Posicao origem, Posicao destino, Jogador jogador) {
        Tabuleiro simulado = tabuleiro.clonar();
        simulado.moverPeca(origem, destino, jogador);

        for (Peca p : simulado.getPecasPorCor(jogador.getCor())) {
            if (p instanceof Rei rei) {
                rei.verificaXeque(simulado);
                return !rei.estaEmXeque();
            }
        }
        return false;
    }
    
    public boolean reiEmXequeAposMovimento(Jogador jogador) {
        for (Peca p : tabuleiro.getPecasPorCor(jogador.getCor())) {
            if (p instanceof Rei rei) {
                rei.verificaXeque(tabuleiro);
                return rei.estaEmXeque();
            }
        }
        return false;
    }
    
    public ArrayList<Posicao> getMovValidos(Jogador j, Posicao origem) {
    	//System.out.println("\n===== TURNO DO JOGADOR " + j.getNome() + " =====");
    	movimentosValidos.clear();
        if (origem == null) return null;
        
        Peca peca = tabuleiro.getPeca(origem);
        System.out.printf("Tipo da peça: %s\n",peca.getTipoPeca());
        System.out.printf("Cor da peça: %s\n", peca.getCor());
        if (!validarPecaSelecionada(origem, j)) return null;
        
        movimentosValidos = obterMovimentosValidos(peca, j);
        if (movimentosValidos.isEmpty()) {
        	System.out.println("\nEssa peça não tem movimentos válidos.");
            return null;
        }
        notificarObservers();
        exibirMovimentos(movimentosValidos);

    	return movimentosValidos;
    }
 
    /**
     * Verifica se o rei do jogador atual está em xeque.
     */
    public void verificarXeque(Jogador jogadorAtual) {
        jogadorAtual.emXeque = false;
        for (Peca p : tabuleiro.getPecasPorCor(jogadorAtual.getCor())) {
            if (p instanceof Rei rei) {
                rei.verificaXeque(tabuleiro);
                if (rei.estaEmXeque()) {
                    jogadorAtual.emXeque = true;
                    existeMovimentoQueTiraReiDoXeque(jogadorAtual);
                    System.out.println(">>> SEU REI ESTÁ EM XEQUE! <<<\n");
                }
            }
        }
    }

    /**
     * Verifica se o movimento atual colocou o adversário em xeque.
     */
    public void verificarXequeAdversario(Jogador jogadorAtual) {
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
    public void existeMovimentoQueTiraReiDoXeque(Jogador jogadorAtual) {
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
    public boolean validarPecaSelecionada(Posicao origem, Jogador jogadorAtual) {
    	Peca peca = tabuleiro.getPeca(origem);
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
    public ArrayList<Posicao> obterMovimentosValidos(Peca peca, Jogador jogadorAtual) {
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
    
    public boolean moverPeca(Posicao origem, Posicao destino, Jogador J) {
    	return tabuleiro.moverPeca(origem, destino, J);

    }
    
    public boolean verificarPromocaoPeao(Posicao pos) {
    	if (pos.getLinha() == 0 || pos.getLinha() == 7) {
    		Peca p = tabuleiro.getPeca(pos);
    		if(p instanceof Peao) {
    			System.out.println("\n***** PROMOÇÃO DO PEAO ***** \n");
                return true;
    		}
        }
    	return false;
    }
    
    public List<String> exportarEstadoJogo() {
        List<String> estado = new ArrayList<>();
        Tabuleiro tabuleiro = this.getTabuleiro();

        for (int lin = 0; lin < 8; lin++) {
            for (int col = 0; col < 8; col++) {
                Peca peca = tabuleiro.getPeca(new Posicao(lin, col));
                if (peca != null) {
                    String linha = String.format("%s;%d;%d;%s", peca.getTipoPeca(), lin, col, peca.getCor());
                    estado.add(linha);
                }
            }
        }

        return estado;
    }
    
    public void promoverPeao(Posicao pos, String tipoEscolhido) {
        Peca peca_antiga = tabuleiro.getPeca(pos);

        if (!(peca_antiga instanceof Peao)) return;

        String nome_cor = peca_antiga.getCor();
        Peca novaPeca = null;
        int n = 0;
        if(nome_cor.equals("P")) {
        	n = 1;
        }
        
        switch (tipoEscolhido) {
            case "Dama":
                novaPeca = new Dama(pos, n);
                break;
            case "Torre":
                novaPeca = new Torre(pos, n);
                break;
            case "Bispo":
                novaPeca = new Bispo(pos, n);
                break;
            case "Cavalo":
                novaPeca = new Cavalo(pos, n);
                break;
            default:
                System.out.println("Tipo de promoção inválido: " + tipoEscolhido);
                return;
        }

        tabuleiro.promovePeca(peca_antiga, novaPeca, pos);
        System.out.println("Peão promovido a " + tipoEscolhido + " na posição " + pos);
        notificarObservers(); // Atualiza a view
    }


    public String[][] getEstadoTabuleiro() {
        String[][] estado = new String[8][8];
        
        for (int linha = 0; linha < 8; linha++) {
            for (int coluna = 0; coluna < 8; coluna++) {
                Peca peca = tabuleiro.getPeca(new Posicao(linha, coluna));
                estado[linha][coluna] = converterPecaParaString(peca);
            }
        }
        return estado;
    }

    private String converterPecaParaString(Peca peca) {
        if (peca == null) return null;
        
        String cor = peca.getCor() == "C" ? "Cyan" : "Purple";
        String tipo = "";
        
        if (peca instanceof Rei) tipo = "K";
        else if (peca instanceof Dama) tipo = "Q";
        else if (peca instanceof Torre) tipo = "R";
        else if (peca instanceof Bispo) tipo = "B";
        else if (peca instanceof Cavalo) tipo = "N";
        else if (peca instanceof Peao) tipo = "P";
        
        return cor + tipo;
   }
    public boolean verificaCongelamento(Jogador jogadorAtual) {
        if(jogadorAtual.emXeque == true) {
        	return false;
        };

        for (Peca peca : tabuleiro.getPecasPorCor(jogadorAtual.getCor())) {
            ArrayList<Posicao> movimentos = obterMovimentosValidos(peca, jogadorAtual);
            if (!movimentos.isEmpty()) {
                return false;
            }
        }
        System.out.println("empate");
        return true;
    }

}
    


