package project251.xadrez.controller;

import javax.swing.*;

import project251.xadrez.model.Jogador;
import project251.xadrez.model.Posicao;
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

  
    public String[][] getEstadoTabuleiro() {
        return jogo.getEstadoTabuleiro();
    }

    public ArrayList<Posicao> obterMovimentosValidos() {
        return movimentosValidos;
    }
}
