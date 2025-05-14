package project251.xadrez.model.api;

import project251.xadrez.model.tabuleiro.Posicao;
import project251.xadrez.model.figura.*;
import project251.xadrez.model.tabuleiro.*;

public class XadrezFacade {

    public static void main(String[] args) {
        Tabuleiro tabuleiro = new Tabuleiro();
        tabuleiro.comecaJogo();
        tabuleiro.exibirTabuleiro();
        
        Posicao peao_preto = new Posicao("a2");
        System.out.println(tabuleiro.getPeca(peao_preto) + ": " + 
        		tabuleiro.getPeca(peao_preto).movValidos(tabuleiro).toString());
  	
	}
}
