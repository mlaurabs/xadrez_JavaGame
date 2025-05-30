package project251.xadrez.model.tests;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;

import project251.xadrez.model.api.Jogador;
import project251.xadrez.model.figura.Cavalo;
import project251.xadrez.model.figura.Peca;
import project251.xadrez.model.figura.Peao;
import project251.xadrez.model.tabuleiro.Posicao;
import project251.xadrez.model.tabuleiro.Tabuleiro;

import java.util.List;

/**
 * Os testes consideram:
 * - Movimentos em "L" (2 casas em uma direção + 1 casa perpendicular)
 * - Capacidade de pular sobre outras peças
 * - Restrição a movimentos não permitidos
 */
public class TesteCavalo {

    private Tabuleiro tabuleiro;

    /**
     * Configura o ambiente de testes inicializando um tabuleiro vazio.
     */
    @Before
    public void setup() {
        tabuleiro = new Tabuleiro(); // Tabuleiro vazio
    }

    /**
     * Verifica os movimentos válidos de um Cavalo no centro do tabuleiro.
     * Deve incluir todas as 8 possíveis posições em "L".
     */
    @Test
    public void testeMovimentosValidos_CavaloNoCentro() {
        Posicao posOrigem = new Posicao("d4");
        Cavalo cavalo = new Cavalo(posOrigem, 0); // Cavalo branco
        tabuleiro.colocarPeca(cavalo, posOrigem);

        List<Posicao> movimentosValidos = cavalo.movValidos(tabuleiro);

        // Verifica quantidade de movimentos (8 no centro do tabuleiro)
        assertEquals(8, movimentosValidos.size());

        // Verifica todos os movimentos em L
        assertTrue(contemPosicao(movimentosValidos, "c2")); // 1
        assertTrue(contemPosicao(movimentosValidos, "e2")); // 2
        assertTrue(contemPosicao(movimentosValidos, "b3")); // 3
        assertTrue(contemPosicao(movimentosValidos, "f3")); // 4
        assertTrue(contemPosicao(movimentosValidos, "b5")); // 5
        assertTrue(contemPosicao(movimentosValidos, "f5")); // 6
        assertTrue(contemPosicao(movimentosValidos, "c6")); // 7
        assertTrue(contemPosicao(movimentosValidos, "e6")); // 8
    }

    /**
     * Verifica que o Cavalo pode pular sobre outras peças,
     * independentemente da cor.
     */
    @Test
    public void testePodePularPecas() {
        Cavalo cavalo = new Cavalo(new Posicao("b1"), 0);
        // Peças bloqueando ao redor
        new Peao(new Posicao("a1"), 0);
        new Peao(new Posicao("b2"), 0);
        new Peao(new Posicao("c1"), 1);

        tabuleiro.colocarPeca(cavalo, new Posicao("b1"));
        tabuleiro.colocarPeca(new Peao(new Posicao("a1"), 0), new Posicao("a1"));
        tabuleiro.colocarPeca(new Peao(new Posicao("b2"), 0), new Posicao("b2"));
        tabuleiro.colocarPeca(new Peao(new Posicao("c1"), 1), new Posicao("c1"));

        List<Posicao> movimentos = cavalo.movValidos(tabuleiro);

        // Deve poder saltar para estas posições mesmo com peças ao redor
        assertTrue(contemPosicao(movimentos, "a3"));
        assertTrue(contemPosicao(movimentos, "c3"));
        assertTrue(contemPosicao(movimentos, "d2"));
    }


    /**
     * Verifica que o Cavalo não pode se mover em linhas retas.
     */
    public void testeMovimentoRetoInvalido() {
        Cavalo cavalo = new Cavalo(new Posicao("b1"), 0);
        tabuleiro.colocarPeca(cavalo, new Posicao("b1"));

        // Tentativa de movimento horizontal (inválido)
        assertFalse(tabuleiro.moverPeca(new Posicao("b1"), new Posicao("d1"), Jogador.B));
    }

    /**
     * Verifica os movimentos válidos de um Cavalo no canto do tabuleiro.
     */
    @Test
    public void testeMovimentosValidos_CavaloNoCanto() {
        Cavalo cavalo = new Cavalo(new Posicao("a1"), 0);
        tabuleiro.colocarPeca(cavalo, new Posicao("a1"));

        List<Posicao> movimentosValidos = cavalo.movValidos(tabuleiro);

        // Apenas 2 movimentos possíveis no canto
        assertEquals(2, movimentosValidos.size());

        // Verifica movimentos específicos
        assertTrue(contemPosicao(movimentosValidos, "b3"));
        assertTrue(contemPosicao(movimentosValidos, "c2"));
    }


    // Método auxiliar para verificar se uma lista contém uma posição
    private boolean contemPosicao(List<Posicao> movimentos, String notacao) {
        Posicao alvo = new Posicao(notacao);
        return movimentos.stream().anyMatch(p -> p.equals(alvo));
    }
}