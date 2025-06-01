package project251.xadrez.controller;

import javax.swing.*;

import project251.xadrez.model.Jogador;
//import project251.xadrez.model.Peca;
import project251.xadrez.model.Posicao;
//import project251.xadrez.model.Tabuleiro;
import project251.xadrez.model.TabuleiroObserver;
import project251.xadrez.model.XadrezFacade;

import java.util.ArrayList;

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
                    movimentosValidos.clear();
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
                    jogadorAtual = jogadorAtual.proximo();
                }
            }
            // Reset após tentativa de movimento
            origemSelecionada = null;
            if(!movimentosValidos.isEmpty()) {
            	movimentosValidos.clear();
            }
            jogo.notificarObservers();
        }
    }

    
	/*
	 * public static void salvarEstadoJogo(XadrezFacade facade, java.io.File
	 * arquivo) { try (java.io.PrintWriter writer = new
	 * java.io.PrintWriter(arquivo)) { Tabuleiro tabuleiro = facade.getTabuleiro();
	 * for (int lin = 0; lin < 8; lin++) { for (int col = 0; col < 8; col++) { Peca
	 * peca = tabuleiro.getPeca(new Posicao(lin, col)); if (peca != null) {
	 * writer.printf("%s;%d;%d;%s%n", peca.getTipoPeca(), lin, col, peca.getCor());
	 * } } } JOptionPane.showMessageDialog(null, "Jogo salvo com sucesso!"); } catch
	 * (Exception ex) { ex.printStackTrace(); JOptionPane.showMessageDialog(null,
	 * "Erro ao salvar: " + ex.getMessage()); }
	 */

  
    public String[][] getEstadoTabuleiro() {
        return jogo.getEstadoTabuleiro();
    }

    public ArrayList<Posicao> obterMovimentosValidos() {
        return movimentosValidos;
    }
}
