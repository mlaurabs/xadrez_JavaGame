package project251.xadrez.fake_controller;

import project251.xadrez.model.api.XadrezFacade;

public class Main {
	public static void main(String[] args) {
	    XadrezFacade jogo = XadrezFacade.getInstance(); // Única instância
	    jogo.iniciarJogo();
	}
}
