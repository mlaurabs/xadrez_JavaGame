package project251.xadrez.model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Map;



/**
 * Classe principal que controla o fluxo do jogo de xadrez.
 * Implementa o padrão Singleton para garantir uma única instância.
 * Responsável por gerenciar turnos, movimentos e regras do jogo.
 */
public class XadrezFacade implements TabuleiroObservado {
    private final Tabuleiro tabuleiro; 
    private final Scanner scanner;
    private ArrayList<Posicao> movimentosValidos = new ArrayList<>();
	private List<TabuleiroObserver> observers = new ArrayList<>();

	/**
	 * Retorna a instância atual do tabuleiro de jogo.
	 * @return Tabuleiro em uso no momento.
	 */

	public Tabuleiro getTabuleiro() {
		return tabuleiro;
	}


    private XadrezFacade() {
        this.tabuleiro = new Tabuleiro();
        this.tabuleiro.comecaJogo();
        this.scanner = new Scanner(System.in);
    }


    private static XadrezFacade instance = null; 


    /**
     * Retorna a instância única (singleton) da fachada do jogo.
     * Se ainda não foi criada, instancia um novo objeto.
     * @return Instância única da classe XadrezFacade.
     */

    public static XadrezFacade getInstance() { 
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
    
    
    /**
     * Reinicia o jogo de xadrez para o estado inicial padrão.
     * Limpa e reinicia o tabuleiro e os estados dos jogadores.
     */

    public void reiniciaJogo() {
    	this.tabuleiro.comecaJogo();
    	Jogador.reiniciarPartida();
    	notificarObservers();
    }
    
    /**
     * Verifica se a movimentação do rei corresponde a uma tentativa de roque.
     * @param origem Posição inicial do rei.
     * @param destino Posição de destino do rei.
     * @return true se corresponde ao movimento de roque; false caso contrário.
     */

    public boolean tentouRoque(Posicao origem, Posicao destino) {
        Peca p = tabuleiro.getPeca(origem);
        if (!(p instanceof Rei)) return false;

        int linha = origem.getLinha();

        // Roque pequeno: rei vai para coluna 6
        if (destino.equals(new Posicao(linha, 6))) {
            return true;
        }

        // Roque grande: rei vai para coluna 2
        if (destino.equals(new Posicao(linha, 2))) {
            return true;
        }

        return false;
    }



    /**
     * Realiza o movimento especial de roque (curto ou longo) para o jogador atual.
     * @param origem  Posição inicial do rei.
     * @param destino Posição final do rei (coluna 6 para roque pequeno, coluna 2 para roque grande).
     * @param jogador Jogador que está tentando realizar o roque.
     * @return true se o roque for realizado com sucesso; false caso contrário.
     */
    public boolean realizarRoque(Posicao origem, Posicao destino, Jogador jogador) {
        System.out.printf("\n[realizarRoque] chamada\n");

        Peca rei = tabuleiro.getPeca(origem);
        if (!(rei instanceof Rei r)) return false;

        int linha = origem.getLinha();

        // Determina se é roque pequeno ou grande baseado na coluna destino
        boolean pequeno = destino.equals(new Posicao(linha, 6)); // G
        boolean grande = destino.equals(new Posicao(linha, 2));  // C

        if (!pequeno && !grande) return false;

        // Posição da torre original
        Posicao posicaoTorreOrigem = pequeno ? new Posicao(linha, 7) : new Posicao(linha, 0);
        Peca torreOriginal = tabuleiro.getPeca(posicaoTorreOrigem);

        if (!(torreOriginal instanceof Torre)) return false;

        // Remove rei e torre de suas posições originais
        tabuleiro.removerPeca(origem);
        tabuleiro.removerPeca(posicaoTorreOrigem);

        // Nova posição do rei
        Posicao novaPosicaoRei = pequeno ? new Posicao(linha, 6) : new Posicao(linha, 2);
        tabuleiro.colocarPeca(rei, novaPosicaoRei);
        r.setPosicao(novaPosicaoRei);

        // Nova posição da torre
        Posicao novaPosicaoTorre = pequeno ? new Posicao(linha, 5) : new Posicao(linha, 3);
        tabuleiro.colocarPeca(torreOriginal, novaPosicaoTorre);
        torreOriginal.setPosicao(novaPosicaoTorre);

        notificarObservers();
        // Atualiza o estado do jogador (desativa futuros roques)
        jogador.reiMoveu = true;
        if (pequeno) {
            jogador.torreDireitaMoveu = true;
        } else {
            jogador.torreEsquerdaMoveu = true;
        }
        
        return true;
    }


    /**
     * Verifica se o rei do jogador especificado está atualmente em xeque.
     * Um rei está em xeque quando está sob ameaça de captura em seu turno atual.
     *
     * @param jogador Jogador a ser verificado.
     * @return true se o rei estiver em xeque; false caso contrário.
     */
    public boolean estaEmXeque(Jogador jogador) {
        for (Peca p : tabuleiro.getPecasPorCor(jogador.getCor())) {
            if (p instanceof Rei rei) {
                rei.verificaXeque(tabuleiro);
                return rei.estaEmXeque();
            }
        }
        return false;
    }
    
    /**
     * Verifica se o jogador especificado está em situação de xeque-mate.
     * @param jogador Jogador a ser verificado.
     * @return true se for xeque-mate; false caso contrário.
     */
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
    
    
    /**
     * Retorna a lista de movimentos válidos para a peça localizada na posição de origem
     * @param j Jogador que está solicitando os movimentos.
     * @param origem Posição atual da peça.
     * @return Lista de posições possíveis de destino, ou null se não houver movimentos válidos.
     */
    public ArrayList<Posicao> getMovValidos(Jogador j, Posicao origem) {
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
     * Verifica se o rei do jogador atual está em xeque e se há algum movimento
     * que possa livrá-lo da ameaça. Atualiza os estados internos do jogador:
     * - `emXeque`: true se o rei estiver em xeque.
     * - `xeque_mate`: true se não houver movimentos válidos para sair do xeque.
     * 
     * Se houver pelo menos um movimento que retire o rei da ameaça,
     * o xeque-mate é evitado e os movimentos possíveis são atualizados.
     *
     * @param jogadorAtual Jogador a ser verificado.
     */
    public void verificarXeque(Jogador jogadorAtual) {
        jogadorAtual.emXeque = false;
        jogadorAtual.xeque_mate = true;

        for (Peca p : tabuleiro.getPecasPorCor(jogadorAtual.getCor())) {
            if (p instanceof Rei rei) {
                rei.verificaXeque(tabuleiro);
                if (rei.estaEmXeque()) {
                    jogadorAtual.emXeque = true;
                    break;
                }
            }
        }

        if (!jogadorAtual.emXeque) {
            jogadorAtual.xeque_mate = false;
            return;
        }

        Tabuleiro tabuleiroAux = tabuleiro.clonar();
        ArrayList<Peca> pecas = tabuleiro.getPecasPorCor(jogadorAtual.getCor());

        for (Peca peca : pecas) {
            Posicao origem = peca.getPosicao();
            ArrayList<Posicao> movimentos = peca.movimentos;

            for (Posicao destino : movimentos) {
                Tabuleiro simulado = tabuleiroAux.clonar();
                simulado.moverPeca(origem, destino, jogadorAtual);

                for (Peca outra : simulado.getPecasPorCor(jogadorAtual.getCor())) {
                    if (outra instanceof Rei rei) {
                        rei.verificaXeque(simulado);
                        if (!rei.estaEmXeque()) {
                            jogadorAtual.xeque_mate = false;

                            ArrayList<Posicao> new_movimentos = new ArrayList<>();
                            new_movimentos.add(destino);
                            peca.setMovimentos(new_movimentos);
                            break;
                        } else {
                            peca.setMovimentos(new ArrayList<>());
                        }
                    }
                }
            }
        }
        if (jogadorAtual.emXeque) {
            System.out.println(">>> SEU REI ESTÁ EM XEQUE! <<<\n");
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
        ArrayList<Posicao> movimentosPossiveis = new ArrayList<>();

        // Gera movimentos padrão (sem roque)
        peca.movValidos(tabuleiro);

        for (Posicao destino : peca.movimentos) {
            Tabuleiro simulado = tabuleiro.clonar();
            simulado.moverPeca(peca.getPosicao(), destino, jogadorAtual);

            boolean reiSeguro = true;
            for (Peca p : simulado.getPecasPorCor(jogadorAtual.getCor())) {
                if (p instanceof Rei rei) {
                    rei.verificaXeque(simulado);
                    if (rei.estaEmXeque()) {
                        reiSeguro = false;
                        break;
                    }
                }
            }

            if (reiSeguro) {
                movimentosPossiveis.add(destino); // Só adiciona se o rei ficar seguro
            }
        }

        //  Verifica se a peça é um Rei e adiciona o roque se permitido
        if (peca instanceof Rei rei) {
            if (rei.RoquePequenoValido(tabuleiro, jogadorAtual)) {
                movimentosPossiveis.add(new Posicao(jogadorAtual.getLinhaInicial(), 6)); // G
            }
            if (rei.RoqueGrandeValido(tabuleiro, jogadorAtual)) {
                movimentosPossiveis.add(new Posicao(jogadorAtual.getLinhaInicial(), 2)); // C
            }
        }

        return movimentosPossiveis;
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
    }

    
    /**
     * Realiza a movimentação de uma peça do jogador, atualizando o tabuleiro,
     * registrando capturas e notificando observadores.
     * Também atualiza os estados do jogador em relação ao movimento de rei ou torre.
     *
     * @param origem Posição inicial da peça.
     * @param destino Posição final desejada.
     * @param j Jogador que está realizando a jogada.
     * @return true se o movimento for bem-sucedido; false caso contrário.
     */
    public boolean moverPeca(Posicao origem, Posicao destino, Jogador j) {
    	System.out.printf("\n>>>moverPeca()\n");
    	Peca capturada = tabuleiro.getPeca(destino);  // salva peça capturada antes
        boolean sucesso = tabuleiro.moverPeca(origem, destino, j);
        if (sucesso) {
        	System.out.printf("\n****Sucesso****\n");
        	
        	if (capturada != null) {
                j.adicionarPecaCapturada(capturada.getTipoPeca());  // atualiza placar
            }
        	
        	Peca peca = tabuleiro.getPeca(destino); 
        	// se a peca movida eh torre ou rei
        	if (peca instanceof Rei) {
        	    j.reiMoveu = true;
        	}
        	if (peca instanceof Torre) {
        		System.out.printf("Eh instancia de torre\n");
        	    int coluna = origem.getColuna();
        	    System.out.printf("Coluna %d\n", coluna);
        	    if (coluna == 0) {
        	        j.torreEsquerdaMoveu = true;
        	        System.out.printf("Torre esquerda ja se moveu\n");
        	    } else if (coluna == 7) {
        	        j.torreDireitaMoveu = true;
        	        System.out.printf("Torre direita ja se moveu\n");
        	    }
        	}

            notificarObservers();
        }
        return sucesso;
    }
    
    /**
     * Verifica se um peão alcançou a última linha do tabuleiro
     * e deve ser promovido.
     * @param pos Posição do peão.
     * @return true se a promoção for possível; false caso contrário.
     */
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
    
    /**
     * Exporta o estado atual do jogo em uma lista de strings,
     * contendo o jogador atual, seu estado e todas as peças com suas posições e cores.
     * @param jogadorAtual Jogador que deve jogar a próxima rodada.
     * @return Lista representando o estado do jogo.
     */
    public List<String> exportarEstadoJogo(Jogador jogadorAtual) {
        List<String> estado = new ArrayList<>();

        // Jogador atual
        estado.add("JOGADOR_ATUAL;" + jogadorAtual.name());

        // Placar de cada jogador
        for (Jogador jogador : Jogador.values()) {
            StringBuilder placar = new StringBuilder("PLACAR_" + jogador.name());
            for (Map.Entry<String, Integer> entry : jogador.getPecasCapturadas().entrySet()) {
                String tipo = entry.getKey(); // Tipo da peça (P, T, C, B, D, R)
                int quantidade = entry.getValue();
                placar.append(";").append(tipo).append(";").append(quantidade);
            }
            estado.add(placar.toString());
        }

        // Estado do jogador C
        estado.add(String.format("JOGADOR_C;%b;%b;%b;%b",
            Jogador.C.emXeque,
            Jogador.C.reiMoveu,
            Jogador.C.torreEsquerdaMoveu,
            Jogador.C.torreDireitaMoveu));

        // Estado do jogador P
        estado.add(String.format("JOGADOR_P;%b;%b;%b;%b",
            Jogador.P.emXeque,
            Jogador.P.reiMoveu,
            Jogador.P.torreEsquerdaMoveu,
            Jogador.P.torreDireitaMoveu));

        // Estado das peças no tabuleiro
        Tabuleiro tabuleiro = this.getTabuleiro();
        for (int lin = 0; lin < 8; lin++) {
            for (int col = 0; col < 8; col++) {
                Peca peca = tabuleiro.getPeca(new Posicao(lin, col));
                if (peca != null) {
                    StringBuilder linhaPeca = new StringBuilder(String.format(
                        "PECA;%s;%d;%d;%s",
                        peca.getTipoPeca(), lin, col, peca.getCor()
                    ));

                    // Se for peão, salvar também se já se moveu
                    if (peca instanceof Peao) {
                        Peao peao = (Peao) peca;
                        linhaPeca.append(";").append(peao.getJaMoveu());
                    }

                    estado.add(linhaPeca.toString());
                }
            }
        }

        return estado;
    }



    
    /**
     * Substitui um peão por uma nova peça (promoção), de acordo com a escolha do jogador.
     * Atualiza o tabuleiro e notifica observadores.
     * @param pos Posição do peão a ser promovido.
     * @param tipoEscolhido Tipo da nova peça ("Dama", "Torre", "Bispo", "Cavalo").
     */
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
        notificarObservers(); 
    }

    
    /**
     * Retorna uma representação textual simplificada do tabuleiro atual,
     * com base no tipo e cor das peças.
     * @return Matriz de strings com a visualização do estado do tabuleiro.
     */
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

    /**
     * Converte uma peça para uma string representativa, com base no tipo e cor.
     * Utilizada para exportar ou exibir o estado do tabuleiro.
     * @param peca Peça a ser convertida.
     * @return String representando a peça (ex: "CyanK", "PurpleP"), ou null se vazia.
     */
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

    
    /**
     * Verifica se o jogador adversário está em situação de congelamento (afogamento/stalemate).
     * Isso ocorre quando o jogador não está em xeque, mas não possui movimentos válidos.
     * @param jogadorAtual Jogador que acabou de jogar (oponente será verificado).
     * @return true se for um caso de congelamento (empate); false caso contrário.
     */
    public boolean verificaCongelamento(Jogador jogadorAtual) {
        if(jogadorAtual.proximo().emXeque == true) {
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
    
    /**
     * Carrega uma partida salva a partir de um arquivo de texto.
     * Atualiza o tabuleiro com as peças e determina o jogador da vez.
     * @param caminhoArquivo Caminho para o arquivo salvo.
     * @param tabuleiro Tabuleiro onde a partida será carregada.
     * @return Jogador que tem a vez após o carregamento.
     */
    public Jogador carregarPartida(String caminhoArquivo, Tabuleiro tabuleiro) {
        tabuleiro.limparPecas(); // Limpa o tabuleiro antes de carregar
        Jogador jogadorAtual = Jogador.C; // Assume o jogador C como o padrão inicialmente

        try (BufferedReader br = new BufferedReader(new FileReader(caminhoArquivo))) {
            String linha = br.readLine();

            // Verifica e lê o jogador atual
            if (linha != null && linha.startsWith("JOGADOR_ATUAL")) {
                String[] partes = linha.split(";");
                if (partes.length == 2) {
                    jogadorAtual = partes[1].equals("P") ? Jogador.P : Jogador.C;
                    System.out.println("Jogador atual carregado: " + jogadorAtual.name()); // Depuração
                }
            }

            // Processa as linhas do arquivo
            while ((linha = br.readLine()) != null) {
                // Lê o placar das peças capturadas
                if (linha.startsWith("PLACAR_C") || linha.startsWith("PLACAR_P")) {
                    String[] partes = linha.split(";");
                    Jogador jogador = linha.startsWith("PLACAR_C") ? Jogador.C : Jogador.P;

                    for (int i = 1; i < partes.length; i += 2) {
                        String tipo = partes[i];
                        int quantidade = Integer.parseInt(partes[i + 1]);
                        for (int j = 0; j < quantidade; j++) {
                            jogador.adicionarPecaCapturada(tipo);
                        }
                    }
                }

                // Lê o estado dos jogadores (xeque e roques)
                else if (linha.startsWith("JOGADOR_C") || linha.startsWith("JOGADOR_P")) {
                    String[] partes = linha.split(";");
                    Jogador jogador = linha.startsWith("JOGADOR_C") ? Jogador.C : Jogador.P;

                    jogador.emXeque = Boolean.parseBoolean(partes[1]);
                    jogador.reiMoveu = Boolean.parseBoolean(partes[2]);
                    jogador.torreEsquerdaMoveu = Boolean.parseBoolean(partes[3]);
                    jogador.torreDireitaMoveu = Boolean.parseBoolean(partes[4]);
                }

                // Lê as peças no tabuleiro
                else if (linha.startsWith("PECA")) {
                    String[] partes = linha.split(";");

                    if (partes.length >= 5) {
                        char tipo = partes[1].charAt(0);
                        int lin = Integer.parseInt(partes[2]);
                        int col = Integer.parseInt(partes[3]);
                        char jogadorLetra = partes[4].charAt(0);

                        Jogador jogador = (jogadorLetra == 'C') ? Jogador.C : Jogador.P;
                        int cor = jogador.getCor();
                        Posicao pos = new Posicao(lin, col);
                        Peca peca = criarPeca(tipo, pos, cor);

                        if (peca != null) {
                            // Se for peão e a linha tem o campo jaMoveu
                            if (peca instanceof Peao && partes.length == 6) {
                                boolean jaMoveu = Boolean.parseBoolean(partes[5]);
                                ((Peao) peca).setJaMoveu(jaMoveu);
                            }
                            tabuleiro.colocarPeca(peca, pos);
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return jogadorAtual;
    }


    
    /**
     * Cria uma instância de uma peça com base no caractere que representa seu tipo.
     * Utilizado no carregamento de partidas.
     * @param tipo Caractere identificador da peça ('P', 'T', 'C', 'B', 'D', 'R').
     * @param pos Posição inicial da peça.
     * @param cor Inteiro representando a cor (0 para CYON, 1 para PURPLE).
     * @return Instância da peça correspondente, ou null se o tipo for inválido.
     */
    private Peca criarPeca(char tipo, Posicao pos, int cor) {
        return switch (tipo) {
            case 'P' -> new Peao(pos, cor);
            case 'T' -> new Torre(pos, cor);
            case 'C' -> new Cavalo(pos, cor);
            case 'B' -> new Bispo(pos, cor);
            case 'D' -> new Dama(pos, cor);
            case 'R' -> new Rei(pos, cor);
            default -> null;
        };
    }

}
    


