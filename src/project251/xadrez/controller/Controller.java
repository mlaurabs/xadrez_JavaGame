package project251.xadrez.controller;

import javax.swing.*;

import project251.xadrez.model.Jogador;
import project251.xadrez.model.Posicao;
import project251.xadrez.model.TabuleiroObserver;
import project251.xadrez.model.XadrezFacade;
import project251.xadrez.view.PainelTabuleiro;

import java.awt.Component;
import java.awt.Window;
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
    private OnGameEndListener gameEndListener; //ver depois
    
    public void registrarObserver(TabuleiroObserver observer) {
        jogo.addObserver(observer);
    }

  //ver depois
    public interface OnGameEndListener {
        void onGameEnd(String mensagemVencedor);
    }
    
    public void setOnGameEndListener(OnGameEndListener listener) {
        this.gameEndListener = listener;
    }
  //ver depois
    
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
                        	//ver depois
                        	if (gameEndListener != null) {
                                gameEndListener.onGameEnd("Xeque-mate! O jogador " + jogadorAtual.proximo().getNome() + " venceu o jogo!");
                            }
                        	//ver depois
                            
                        } else {
                            JOptionPane.showMessageDialog(null,
                                "O rei do Jogador " + jogadorAtual.getNome() + " está em Xeque.",
                                "Xeque", JOptionPane.INFORMATION_MESSAGE);
                        }
                        
                    }else if(jogo.verificaCongelamento(jogadorAtual)) {
                    	if (gameEndListener != null) {
                            gameEndListener.onGameEnd("O rei do jogador " + jogadorAtual.getNome() + " está afogado. \n A PARTIDA TERMINA EM EMPATE.");
                        }
                    }
                }
            }
            origemSelecionada = null;
            if (movimentosValidos != null && !movimentosValidos.isEmpty()) {
                movimentosValidos.clear();
            }
        }

        //Jogador.imprimirPlacarFormatado();
    }

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

    public String[][] getEstadoTabuleiro() {
        return jogo.getEstadoTabuleiro();
    }

    public ArrayList<Posicao> obterMovimentosValidos() {
        return movimentosValidos;
    }
    
    public void reiniciaJogo() {
    	jogadorAtual = Jogador.C;
    	jogo.reiniciaJogo();
    }
    
    public Jogador getUltimoJogador() {
        return ultimoJogador != null ? ultimoJogador : jogadorAtual.proximo();
    }


    private void voltarParaJanelaInicial(Component componenteReferencia) {
        // Verifica se o componente é um PainelTabuleiro e o encerra explicitamente
        if (componenteReferencia instanceof PainelTabuleiro painel) {
            painel.encerrar(); // Método que você deve implementar no PainelTabuleiro
        }

        // Fecha a janela principal onde o componente está
        Window janela = SwingUtilities.getWindowAncestor(componenteReferencia);
        if (janela instanceof JFrame frame) {
            frame.getContentPane().removeAll(); // Limpa os componentes
            frame.dispose(); // Fecha a janela completamente
        }

        // Reinicia o jogo ou volta à tela principal
        new project251.xadrez.view.JanelaPrincipal();
    }
    
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
