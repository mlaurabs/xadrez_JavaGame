package project251.xadrez.controller;

import javax.swing.*;

import project251.xadrez.model.Jogador;
//import project251.xadrez.model.Peca;
import project251.xadrez.model.Posicao;
//import project251.xadrez.model.Tabuleiro;
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

    public void registrarObserver(TabuleiroObserver observer) {
        jogo.addObserver(observer);
    }

    public void processarClique(int linha, int coluna) {
        Posicao clicada = new Posicao(linha, coluna);

        if (origemSelecionada == null) {
            // Primeiro clique - tentativa de selecionar peça
            if (jogo.validarPecaSelecionada(clicada, jogadorAtual)) {
                movimentosValidos = jogo.getMovValidos(jogadorAtual, clicada);
                if (movimentosValidos == null || movimentosValidos.isEmpty()) {
                    // Nenhum movimento possível para a peça
                    JOptionPane.showMessageDialog(null, "Essa peça não tem movimentos válidos.\nTente outra peça, jogador " + jogadorAtual + "!");
                    origemSelecionada = null;
                } else {
                    origemSelecionada = clicada;
                    jogo.notificarObservers();
                }
            } else {
                JOptionPane.showMessageDialog(null, "Selecione uma peça válida do jogador " + jogadorAtual + "!");
            }
        } else {
            // Segundo clique - tentativa de mover a peça
            if (movimentosValidos.contains(clicada)) {
                if (jogo.moverPeca(origemSelecionada, clicada, jogadorAtual)) {
                	 // VERIFICA PROMOÇÃO DE PEÃO
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
                            jogo.promoverPeao(clicada, escolha); // Implementar esse método na fachada
                        }
                    }
                	jogadorAtual = jogadorAtual.proximo();
                }
            }
            // Reset após tentativa de movimento
            origemSelecionada = null;
            if(movimentosValidos != null || !movimentosValidos.isEmpty()) {
            	movimentosValidos.clear();
            }
            jogo.notificarObservers();
        }

        verificarEstadoPosMovimento();
        Jogador.imprimirPlacarFormatado(); // para conferirmos por enquanto
        
     }


    private void verificarEstadoPosMovimento() {
        jogo.verificarXeque(jogadorAtual);

        if (jogadorAtual.emXeque) {
            if (jogadorAtual.xeque_mate) {
                JOptionPane.showMessageDialog(null, "XEQUE-MATE! Jogador " + jogadorAtual.getNome().toUpperCase() + " perdeu o jogo.");
                System.exit(0);
            } else {
                JOptionPane.showMessageDialog(null, "XEQUE no jogador " + jogadorAtual.getNome() + "!");
            }
        }
    }

    
    public void salvarEstadoJogo(File arquivo) {
        try (PrintWriter writer = new PrintWriter(arquivo)) {
            List<String> estado = jogo.exportarEstadoJogo(); // <- só chama a fachada
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
}
