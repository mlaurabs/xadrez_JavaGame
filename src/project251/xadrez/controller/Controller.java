package project251.xadrez.controller;

import project251.xadrez.model.api.XadrezFacade;
import project251.xadrez.model.figura.Peao;
import project251.xadrez.model.figura.Peca;
import project251.xadrez.model.tabuleiro.Posicao;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class Controller {
    private final XadrezFacade xad = XadrezFacade.getInstance();
    private Posicao origemSelecionada;
    private List<Posicao> movimentosValidos = new ArrayList<>();

    /**
     * Processa um clique em uma casa do tabuleiro
     */
    public void processarClique(int linha, int coluna, Runnable repaintCallback) {
        Posicao clicada = new Posicao(linha, coluna);

        System.out.printf("linha: %d - coluna %d\n", clicada.getLinha(), clicada.getColuna());
    }

}
