package project251.xadrez.model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


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

	public Tabuleiro getTabuleiro() {
		return tabuleiro;
	}

    // 1. Construtor privado
    private XadrezFacade() {
        this.tabuleiro = new Tabuleiro();
        this.tabuleiro.comecaJogo();
        this.scanner = new Scanner(System.in);
    }


    private static XadrezFacade instance = null; 

    // Método público de acesso
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
    
    public void reiniciaJogo() {
    	this.tabuleiro.comecaJogo();
    	Jogador.C.resetarRoque();
    	Jogador.P.resetarRoque();
    	notificarObservers();
    }
    
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



 // Realiza o roque (movimento especial do rei e torre)
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


//manter
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
    
//    public boolean simulaJogadaRetiraXeque(Posicao origem, Posicao destino, Jogador jogador) {
//        Tabuleiro simulado = tabuleiro.clonar();
//        simulado.moverPeca(origem, destino, jogador);
//
//        for (Peca p : simulado.getPecasPorCor(jogador.getCor())) {
//            if (p instanceof Rei rei) {
//                rei.verificaXeque(simulado);
//                return !rei.estaEmXeque();
//            }
//        }
//        return false;
//    }
    
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
     * Verifica se o jogador está em xeque e se há algum movimento que possa tirá-lo da situação.
     * Atualiza os estados `emXeque` e `xeque_mate` do jogador.
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

//    /**
//     * Verifica se o rei do jogador atual está em xeque.
//     */
//    public void verificarXeque(Jogador jogadorAtual) {
//        jogadorAtual.emXeque = false;
//        for (Peca p : tabuleiro.getPecasPorCor(jogadorAtual.getCor())) {
//            if (p instanceof Rei rei) {
//                rei.verificaXeque(tabuleiro);
//                if (rei.estaEmXeque()) {
//                    jogadorAtual.emXeque = true;
//                    existeMovimentoQueTiraReiDoXeque(jogadorAtual);
//                    System.out.println(">>> SEU REI ESTÁ EM XEQUE! <<<\n");
//                }
//            }
//        }
//    }
//    /**
//     * Verifica se existe algum movimento que tire o rei do xeque.
//     * Atualiza o estado de xeque-mate se necessário.
//     */
//    public void existeMovimentoQueTiraReiDoXeque(Jogador jogadorAtual) {
//        jogadorAtual.xeque_mate = true; 
//        Tabuleiro tabuleiroAux = tabuleiro.clonar(); 
//
//        // Pega peças do jogador na cópia
//        ArrayList<Peca> pecas = tabuleiro.getPecasPorCor(jogadorAtual == Jogador.P ? 0 : 1);
//
//        for (Peca peca : pecas) {
//            Posicao origem = peca.getPosicao();
//            ArrayList<Posicao> movimentos = peca.movimentos;
//
//            for (Posicao destino : movimentos) {
//                // Clonar novamente o tabuleiro antes de cada teste (para isolar os efeitos)
//                Tabuleiro simulado = tabuleiroAux.clonar();
//
//                simulado.moverPeca(origem,destino, jogadorAtual); 
//
//                // Verifica se o rei ainda está em xeque após o movimento
//                for (Peca outra : simulado.getPecasPorCor(jogadorAtual.getCor())) {
//                    if (outra instanceof Rei rei) {
//                        rei.verificaXeque(simulado);
//                        if (rei.estaEmXeque()) {
//                            ArrayList<Posicao> vazio = new ArrayList<>();
//                            peca.setMovimentos(vazio);
//                        } else {
//                        	jogadorAtual.xeque_mate = false;
//                            ArrayList<Posicao> new_movimentos = new ArrayList<>();
//                            new_movimentos.add(destino);
//                            peca.setMovimentos(new_movimentos);
//                        }
//                    }
//                }
//            }
//        }
//    }
//    

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

    
    public boolean moverPeca(Posicao origem, Posicao destino, Jogador j) {
    	System.out.printf("\n>>>moverPeca()\n");
        boolean sucesso = tabuleiro.moverPeca(origem, destino, j);
        if (sucesso) {
        	System.out.printf("\n****Sucesso****\n");
        	Peca peca = tabuleiro.getPeca(destino); // se a peca movida eh torre ou rei

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
    
    public List<String> exportarEstadoJogo(Jogador jogadorAtual) {
        List<String> estado = new ArrayList<>();
        
        estado.add(jogadorAtual.name());
        
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
        notificarObservers(); 
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
    
    public Jogador carregarPartida(String caminhoArquivo, Tabuleiro tabuleiro) {
        tabuleiro.limparPecas(); // limpa o tabuleiro antes de carregar
        Jogador jogadorAtual = Jogador.C;
        
        try (BufferedReader br = new BufferedReader(new FileReader(caminhoArquivo))) {
            String linha = br.readLine();
            if (linha != null && (linha.equals("C") || linha.equals("P"))) {
                jogadorAtual = linha.equals("P") ? Jogador.P : Jogador.C;
            }
            while ((linha = br.readLine()) != null) {
                String[] partes = linha.split(";");

                if (partes.length != 4) continue; // ignora linha errada

                char tipo = partes[0].charAt(0);
                int lin = Integer.parseInt(partes[1]);
                int col = Integer.parseInt(partes[2]);
                char jogadorLetra = partes[3].charAt(0); // 'C' ou 'P'

                Jogador jogador = (jogadorLetra == 'C') ? Jogador.C : Jogador.P;
                int cor = jogador.getCor();

                Posicao pos = new Posicao(lin, col);
                Peca peca = criarPeca(tipo, pos, cor);

                if (peca != null) {
                    tabuleiro.colocarPeca(peca, pos);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jogadorAtual;
    }
    
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
    


