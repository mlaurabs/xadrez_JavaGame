package project251.xadrez.controller;

import javax.swing.*;
import project251.xadrez.model.Jogador;
import project251.xadrez.model.Posicao;
import project251.xadrez.model.XadrezFacade;
import project251.xadrez.model.Tabuleiro;
import project251.xadrez.model.Peca;

import java.util.ArrayList;
import java.util.List;

public class Controller {
    private final XadrezFacade xad = XadrezFacade.getInstance();
    private Posicao origemSelecionada;
    private ArrayList<Posicao> movimentosValidos = new ArrayList<>();
    Jogador jogadorAtual = Jogador.C;

    /**
     * Processa um clique em uma casa do tabuleiro
     */
    public void processarClique(int linha, int coluna, Runnable repaintCallback) {
        Posicao clicada = new Posicao(linha, coluna);

        System.out.printf("linha: %d - coluna %d\n", clicada.getLinha(), clicada.getColuna());

        movimentosValidos = xad.processaTurno(jogadorAtual, clicada);
    }

    public ArrayList<Posicao> obterMovimentosValidos() {
        return movimentosValidos;
    }

    public static void salvarEstadoJogo(XadrezFacade facade, java.io.File arquivo) {
        try (java.io.PrintWriter writer = new java.io.PrintWriter(arquivo)) {
            Tabuleiro tabuleiro = facade.getTabuleiro();
            for (int lin = 0; lin < 8; lin++) {
                for (int col = 0; col < 8; col++) {
                    Peca peca = tabuleiro.getPeca(new Posicao(lin, col));
                    if (peca != null) {
                        writer.printf("%s;%d;%d;%s%n", peca.getTipoPeca(), lin, col, peca.getCor());
                    }
                }
            }
            JOptionPane.showMessageDialog(null, "Jogo salvo com sucesso!");
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro ao salvar: " + ex.getMessage());
        }
    }
}
