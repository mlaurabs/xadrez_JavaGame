package project251.xadrez.controller;

import javax.swing.*;

import project251.xadrez.model.Jogador;
import project251.xadrez.model.Posicao;
import project251.xadrez.model.TabuleiroObserver;
import project251.xadrez.model.XadrezFacade;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class Controller {
    private final XadrezFacade jogo = XadrezFacade.getInstance();
    private Posicao origemSelecionada;
    private ArrayList<Posicao> movimentosValidos = new ArrayList<>();
    public Jogador jogadorAtual = Jogador.C;
    public Jogador ultimoJogador = Jogador.C;
    private AvisoDeJogoEncerrado gameEndListener;
    
    public void registrarObserver(TabuleiroObserver observer) {
        jogo.addObserver(observer);
    }

    public interface AvisoDeJogoEncerrado {
        void finalDeJogo(String mensagemVencedor);
    }
    
    public void setOnGameEndListener(AvisoDeJogoEncerrado listener) {
        this.gameEndListener = listener;
    }

    /**
     * Processa o clique do jogador em uma célula do tabuleiro.
     * Primeiro clique seleciona a peça; segundo clique tenta movimentá-la.
     * Trata lógica de roque, promoção de peão, xeque, xeque-mate e empate por afogamento.
     *
     * @param linha Linha clicada no tabuleiro.
     * @param coluna Coluna clicada no tabuleiro.
     */
    public void processarJogada(int linha, int coluna) {
        Posicao clicada = new Posicao(linha, coluna);

        if (origemSelecionada == null) {
            // Primeiro clique - tentativa de selecionar peça
            if (jogo.validarPecaSelecionada(clicada, jogadorAtual)) {
                movimentosValidos = jogo.getMovValidos(jogadorAtual, clicada);
                if (movimentosValidos == null || movimentosValidos.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Essa peça não tem movimentos válidos.\nTente outra peça, jogador " + jogadorAtual.getNome() + "!");
                    origemSelecionada = null;
                } else {
                    origemSelecionada = clicada;
                }
            } else {
                JOptionPane.showMessageDialog(null, "Selecione uma peça válida do jogador " + jogadorAtual.getNome() + "!");
            }
        } else {
            // Segundo clique - tentativa de mover a peça
            if (movimentosValidos.contains(clicada)) { // verifica se o movimento é válido antes de mover a peca
            	ultimoJogador = jogadorAtual;
            	if(jogo.tentouRoque(origemSelecionada, clicada)) {
            		System.out.printf("\ncaptou a tentativa de roque");
            		jogo.realizarRoque(origemSelecionada, clicada, jogadorAtual);
            		jogadorAtual = jogadorAtual.proximo();
            	}
            	else if (jogo.moverPeca(origemSelecionada, clicada, jogadorAtual)) {
                    if (jogo.verificarPromocaoPeao(clicada)) {
                        String[] opcoes = {"Dama", "Torre", "Bispo", "Cavalo"};
                        String escolha = (String) JOptionPane.showInputDialog(null,
                            "Escolha a peça para promover o peão:",
                            "Promoção de Peão",
                            JOptionPane.PLAIN_MESSAGE,
                            null,
                            opcoes,
                            opcoes[0]);

                        if (escolha != null) {
                            jogo.promoverPeao(clicada, escolha);
                        }
                    }

                    jogadorAtual = jogadorAtual.proximo();
                
                    jogo.verificarXeque(jogadorAtual);

                    if (jogadorAtual.emXeque) {
                        if (jogo.ehXequeMate(jogadorAtual)) {
                        	if (gameEndListener != null) {
                                gameEndListener.finalDeJogo("Xeque-mate! O jogador " + jogadorAtual.proximo().getNome() + " venceu o jogo!");
                            }
                            
                        } else {
                            JOptionPane.showMessageDialog(null,
                                "O rei do Jogador " + jogadorAtual.getNome() + " está em Xeque.",
                                "Xeque", JOptionPane.INFORMATION_MESSAGE);
                        }
                        
                    }else if(jogo.verificaCongelamento(jogadorAtual)) {
                    	if (gameEndListener != null) {
                            gameEndListener.finalDeJogo("O rei do jogador " + jogadorAtual.getNome() + " está afogado. \n A PARTIDA TERMINA EM EMPATE.");
                        }
                    }
                }
            }
            origemSelecionada = null;
            if (movimentosValidos != null && !movimentosValidos.isEmpty()) {
                movimentosValidos.clear();
            }
        }

    }

    /**
     * Salva o estado atual do jogo em um arquivo texto.
     * Inclui o jogador da vez e todas as peças com suas posições e cores.
     *
     * @param arquivo Arquivo de destino onde o estado será salvo.
     */
    public void salvarEstadoJogo(File arquivo) {
        try (PrintWriter writer = new PrintWriter(arquivo)) {
            List<String> estado = jogo.exportarEstadoJogo(jogadorAtual);
            for (String linha : estado) {
                writer.println(linha);
            }
            JOptionPane.showMessageDialog(null, "Jogo salvo com sucesso!");
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro ao salvar: " + ex.getMessage());
        }
    }
    
    
    /**
     * Retorna o estado atual do tabuleiro em forma de matriz de strings,
     * para ser usado na visualização gráfica.
     *
     * @return Matriz de 8x8 representando o estado do tabuleiro.
     */
    public String[][] getEstadoTabuleiro() {
        return jogo.getEstadoTabuleiro();
    }

    /**
     * Retorna a lista atual de movimentos válidos da peça selecionada.
     *
     * @return Lista de posições para onde a peça pode se mover.
     */
    public ArrayList<Posicao> obterMovimentosValidos() {
        return movimentosValidos;
    }
    
    /**
     * Reinicia o jogo do zero, limpando o tabuleiro e reiniciando o jogador atual.
     */
    public void reiniciaJogo() {
    	jogadorAtual = Jogador.C;
    	jogo.reiniciaJogo();
    }


    /**
     * Carrega o estado de uma partida previamente salva a partir de um arquivo.
     *
     * @param arquivo Arquivo contendo o estado salvo da partida.
     */
    public void carregarEstadoJogo(File arquivo) {
        try {
            jogadorAtual = jogo.carregarPartida(arquivo.getAbsolutePath(), jogo.getTabuleiro());
            JOptionPane.showMessageDialog(null, "Jogo carregado com sucesso!");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro ao carregar: " + e.getMessage());
        }
    }

}
