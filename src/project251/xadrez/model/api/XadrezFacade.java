package project251.xadrez.model.api;

import project251.xadrez.model.tabuleiro.Posicao;
import project251.xadrez.model.figura.*;
import project251.xadrez.model.tabuleiro.*;

public class XadrezFacade {

    public static void main(String[] args) {
        Tabuleiro tabuleiro = new Tabuleiro();
        tabuleiro.comecaJogo();

        Posicao peca = new Posicao("d5");
        tabuleiro.removerPeca(new Posicao("a2"));
        tabuleiro.colocarPeca(new Dama(new Posicao("d5"),0), new Posicao("d5"));
        tabuleiro.exibirTabuleiro();
        System.out.println(tabuleiro.getPeca(peca) + ": " + 
        		tabuleiro.getPeca(peca).movValidos(tabuleiro).toString());
  	
	}
}
