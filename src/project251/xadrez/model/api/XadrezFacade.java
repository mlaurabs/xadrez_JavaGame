package project251.xadrez.model.api;

import project251.xadrez.model.tabuleiro.Posicao;
import project251.xadrez.model.figura.*;
import project251.xadrez.model.tabuleiro.*;

public class XadrezFacade {

    public static void main(String[] args) {
        Tabuleiro tabuleiro = new Tabuleiro();
        tabuleiro.comecaJogo();

        Posicao peca = new Posicao("b1");;
        tabuleiro.exibirTabuleiro();
        System.out.println(tabuleiro.getPeca(peca) + ": " + 
        		tabuleiro.getPeca(peca).movValidos(tabuleiro).toString());
  	
	}
}
