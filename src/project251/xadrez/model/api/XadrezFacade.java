package project251.xadrez.model.api;

import project251.xadrez.model.tabuleiro.Posicao;
import project251.xadrez.model.tabuleiro.Tabuleiro;
import project251.xadrez.model.figura.Peao;
import project251.xadrez.model.figura.Peca;
import project251.xadrez.model.figura.Rei;

import java.util.List;
import java.util.Scanner;

public class XadrezFacade {

    public static void main(String[] args) {
        Tabuleiro tabuleiro = new Tabuleiro();
        tabuleiro.comecaJogo();
        Scanner scanner = new Scanner(System.in);

        Jogador jogadorAtual = Jogador.B;

        System.out.println("\n============= XADREZ =============");
        System.out.println("\n***** LEGENDA *****");
        System.out.println("\n*Peças*\n");
        System.out.println("D - Dama");
        System.out.println("R - Rei");
        System.out.println("T - Torre");
        System.out.println("B - Bispo");
        System.out.println("C - Cavalo");
        System.out.println("P - Peão");
        System.out.println("\n*Jogadores*\n");
        System.out.println("B - Branco");
        System.out.println("P - Preto");
        System.out.println("\n*Exemplo*\n");
        System.out.println("TB: torre branca\n");
        System.out.println("\n========== INCIANDO JOGO ==========");

        while (true) {

            System.out.println("\n===== TURNO DO JOGADOR " + jogadorAtual.getNome() + " =====");

            boolean emXeque = false;
            for (Peca p : tabuleiro.getPecasPorCor(jogadorAtual == Jogador.B ? 0 : 1)) {
                if (p instanceof Rei rei) {
                    rei.verificaXeque(tabuleiro);
                    if (rei.estaEmXeque()) {
                        emXeque = true;
                        System.out.println(">>> SEU REI ESTÁ EM XEQUE! <<<");
                    }
                }
            }

            if (emXeque && !existeMovimentoQueTiraReiDoXeque(tabuleiro, jogadorAtual)) {
                System.out.println(">>> XEQUE-MATE! Jogador " + jogadorAtual.getNome().toUpperCase() + " perdeu o jogo. <<<");
                System.exit(0);
            }

            tabuleiro.exibirTabuleiro();

            System.out.print("\nDigite a posição da peça que deseja mover (ex: e2): ");
            String origemStr = scanner.nextLine();

            if("sair".equalsIgnoreCase(origemStr)) {
                System.out.println("Jogo Encerrado!");
                System.exit(0);
            }

            Posicao origem;
            try {
                origem = new Posicao(origemStr);
            } catch (IllegalArgumentException e) {
                System.out.println("\nPosição inválida.");
                continue;
            }

            Peca peca = tabuleiro.getPeca(origem);
            if (peca == null) {
                System.out.println("\nNão há peça nessa posição.");
                continue;
            }

            if (!peca.getCor().toString().equalsIgnoreCase(jogadorAtual.toString())) {
                System.out.println("\nVocê só pode mover suas próprias peças!");
                continue;
            }

            List<Posicao> movimentosValidos = peca.movValidos(tabuleiro);
            if (movimentosValidos.isEmpty()) {
                System.out.println("\nEssa peça não tem movimentos válidos.");
                continue;
            }

            System.out.println("\nMovimentos válidos:");
            for (Posicao destino : movimentosValidos) {
                System.out.print(destino + " ");
            }
            System.out.println();

            System.out.print("\nDigite a posição de destino (ex: e4): ");
            String destinoStr = scanner.nextLine();

            Posicao destino;
            try {
                destino = new Posicao(destinoStr);
            } catch (IllegalArgumentException e) {
                System.out.println("\nPosição de destino inválida.");
                continue;
            }

            System.out.println(tabuleiro.moverPeca(origem, destino, jogadorAtual));

            if (peca instanceof Peao && (destino.getLinha() == 0 || destino.getLinha() == 7)) {
                System.out.println("\n***** PEÃO ANTES DA PROMOÇÃO ***** \n");
                tabuleiro.exibirTabuleiro();
                Peca novaPeca = tabuleiro.escolherPromocao(destino, peca.cor, new Scanner(System.in));
                tabuleiro.promovePeca(peca, novaPeca, destino);
            }

            Jogador adversario = jogadorAtual.proximo();
            for (Peca p : tabuleiro.getPecasPorCor(adversario == Jogador.B ? 0 : 1)) {
                if (p instanceof Rei rei) {
                    rei.verificaXeque(tabuleiro);
                    if (rei.estaEmXeque()) {
                        System.out.println("\n>>> O rei das peças " + adversario.getNome().toUpperCase() + " está em XEQUE! <<<");
                    }
                }
            }

            Jogador.imprimirPlacarFormatado();
            jogadorAtual = jogadorAtual.proximo();
        }
    }

    // esse metodo faz a verificação se tem algum movimento que é capaz de acabar com o xeque

    public static boolean existeMovimentoQueTiraReiDoXeque(Tabuleiro tabuleiro, Jogador jogador) {
        List<Peca> pecas = tabuleiro.getPecasPorCor(jogador == Jogador.B ? 0 : 1);

        for (Peca peca : pecas) {
            Posicao origem = peca.getPosicao();
            List<Posicao> movimentos = peca.movValidos(tabuleiro);

            for (Posicao destino : movimentos) {
                // Salva peça capturada (pode ser null)
                Peca capturada = tabuleiro.getPeca(destino);

                // Remove peça da origem
                tabuleiro.colocarPeca(null, origem);

                // Remove peça que está no destino, se existir
                if (capturada != null) {
                    tabuleiro.colocarPeca(null, destino);
                }

                // Coloca peça no destino
                tabuleiro.colocarPeca(peca, destino);
                peca.setPosicao(destino);

                // Verifica se rei ainda está em xeque após o movimento
                boolean reiEmXeque = false;
                for (Peca outra : tabuleiro.getPecasPorCor(jogador == Jogador.B ? 0 : 1)) {
                    if (outra instanceof Rei rei) {
                        rei.verificaXeque(tabuleiro);
                        reiEmXeque = rei.estaEmXeque();
                    }
                }

                // Desfaz o movimento
                tabuleiro.colocarPeca(null, destino);
                tabuleiro.colocarPeca(peca, origem);
                peca.setPosicao(origem);

                if (capturada != null) {
                    tabuleiro.colocarPeca(capturada, destino);
                    capturada.setPosicao(destino);
                }

                if (!reiEmXeque) return true;
            }
        }

        return false;
    }
}
