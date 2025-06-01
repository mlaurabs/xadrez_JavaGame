package project251.xadrez.controller;

import javax.swing.*;

import project251.xadrez.model.Jogador;
import project251.xadrez.model.Posicao;
import project251.xadrez.model.TabuleiroObserver;
import project251.xadrez.model.XadrezFacade;

import java.util.ArrayList;
import java.util.List;

public class Controller {
    private final XadrezFacade jogo = XadrezFacade.getInstance();
    private Posicao origemSelecionada;
    private ArrayList<Posicao> movimentosValidos = new ArrayList<>();
    Jogador jogadorAtual = Jogador.P;

     // Método para registrar observers
    public void registrarObserver(TabuleiroObserver observer) {
        jogo.addObserver(observer);
    }
    
    
    /**
     * Processa um clique em uma casa do tabuleiro
     */
    public void processarClique(int linha, int coluna, Runnable repaintCallback) {
        Posicao clicada = new Posicao(linha, coluna);

        System.out.printf("linha: %d - coluna %d\n", clicada.getLinha(), clicada.getColuna());
 
        
    	movimentosValidos = jogo.processaTurno(jogadorAtual, clicada);
        //jogo.notificarObservers();
        
    }
    
	
	public ArrayList<Posicao> obterMovimentosValidos() { return
			movimentosValidos; }
	 
	

	 
 

}
