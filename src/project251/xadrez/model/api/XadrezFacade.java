package project251.xadrez.model.api;

import project251.xadrez.model.tabuleiro.Posicao;

public class XadrezFacade {

    public static void main(String[] args) {
        Posicao p = new Posicao("e2");
        System.out.println("Posição: " + p); // Deve imprimir "e2"
        System.out.println("Linha: " + p.getLinha());  // Deve imprimir 6
        System.out.println("Coluna: " + p.getColuna()); // Deve imprimir 4
    }
}
