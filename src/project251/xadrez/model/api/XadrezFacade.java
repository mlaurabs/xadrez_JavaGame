package project251.xadrez.model.api;

import project251.xadrez.model.tabuleiro.Posicao;
import project251.xadrez.model.tabuleiro.Tabuleiro;
import project251.xadrez.model.figura.Peca;

import java.util.List;
import java.util.Scanner;

public class XadrezFacade {

    public static void main(String[] args) {
        Tabuleiro tabuleiro = new Tabuleiro();
        tabuleiro.comecaJogo();
        Scanner scanner = new Scanner(System.in);

        Jogador jogadorAtual = Jogador.B;

        while (true) {
            System.out.println("\n===== TURNO DO JOGADOR " + jogadorAtual + " =====");
            tabuleiro.exibirTabuleiro();

            System.out.print("Digite a posição da peça que deseja mover (ex: e2): ");
            String origemStr = scanner.nextLine();

            Posicao origem;
            try {
                origem = new Posicao(origemStr);
            } catch (IllegalArgumentException e) {
                System.out.println("Posição inválida.");
                continue;
            }

            Peca peca = tabuleiro.getPeca(origem);
            if (peca == null) {
                System.out.println("Não há peça nessa posição.");
                continue;
            }

            System.out.println("jogador atual " + jogadorAtual.toString());
            if (!peca.getCor().toString().equalsIgnoreCase(jogadorAtual.toString())) {
                System.out.println("Você só pode mover suas próprias peças!");
                continue;
            }

            List<Posicao> movimentosValidos = peca.movValidos(tabuleiro);
            if (movimentosValidos.isEmpty()) {
                System.out.println("Essa peça não tem movimentos válidos.");
                continue;
            }

            System.out.println("Movimentos válidos:");
            for (Posicao destino : movimentosValidos) {
                System.out.print(destino + " ");
            }
            System.out.println();

            System.out.print("Digite a posição de destino (ex: e4): ");
            String destinoStr = scanner.nextLine();
            
            System.out.println(destinoStr);
            Posicao destino;
            try {
                destino = new Posicao(destinoStr);
                System.out.println("linha: " + destino.getLinha());
                System.out.println("coluna: " + destino.getColuna());
            } catch (IllegalArgumentException e) {
                System.out.println("Posição de destino inválida.");
                continue;
            }

			/*
			 * if (!movimentosValidos.contains(destino)) {
			 * System.out.println("Movimento inválido para essa peça."); continue; }
			 */

            tabuleiro.moverPeca(origem, destino);
            jogadorAtual = jogadorAtual.proximo(); // alterna turno
        }
    }
}
