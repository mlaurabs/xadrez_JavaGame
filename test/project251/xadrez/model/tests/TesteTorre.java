package project251.xadrez.model.tests;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;

import project251.xadrez.model.api.Jogador;
import project251.xadrez.model.figura.Torre;
import project251.xadrez.model.figura.Peca;
import project251.xadrez.model.tabuleiro.Posicao;
import project251.xadrez.model.tabuleiro.Tabuleiro;

import java.util.List;

/**
 * Classe de testes para verificar o comportamento do movimento das Torres no tabuleiro de xadrez.
 * 
 * Os testes consideram:
 * - Movimentos horizontais e verticais ilimitados
 * - Bloqueio por peças da mesma cor
 * - Captura de peças adversárias
 */
public class TesteTorre {

    private Tabuleiro tabuleiro;

    /**
     * Configura o ambiente de testes inicializando um tabuleiro vazio.
     * Diferente dos testes de Peão, não usamos a configuração inicial do jogo
     * para ter mais controle sobre as posições das peças.
     */
    @Before
    public void setup() {
        tabuleiro = new Tabuleiro(); // Tabuleiro vazio
    }

    /**
     * Verifica os movimentos válidos de uma Torre em um tabuleiro vazio.
     * Deve incluir todas as posições na mesma linha e coluna.
     */
    @Test
    public void testeMovimentosValidos_TorreEmTabuleiroVazio() {
        Posicao posOrigem = new Posicao("d4");
        Torre torre = new Torre(posOrigem, 0); // Torre branca
        tabuleiro.colocarPeca(torre, posOrigem);

        List<Posicao> movimentosValidos = torre.movValidos(tabuleiro);

        // Deve ter 14 movimentos possíveis (7 na horizontal + 7 na vertical)
        assertEquals(14, movimentosValidos.size());

        // Verifica alguns movimentos esperados
        assertTrue(contemPosicao(movimentosValidos, "a4")); // Horizontal esquerda
        assertTrue(contemPosicao(movimentosValidos, "h4")); // Horizontal direita
        assertTrue(contemPosicao(movimentosValidos, "d1")); // Vertical baixo
        assertTrue(contemPosicao(movimentosValidos, "d8")); // Vertical cima
    }

    /**
     * Verifica que a Torre é bloqueada por peças da mesma cor
     * e pode capturar peças adversárias.
     */
    @Test
    public void testeMovimentosComBloqueio() {
        // Configuração
        Torre torreBranca = new Torre(new Posicao("a1"), 0);
        new Torre(new Posicao("a4"), 0); // Torre aliada (bloqueio)
        new Torre(new Posicao("c1"), 1); // Torre adversária (captura possível)

        tabuleiro.colocarPeca(torreBranca, new Posicao("a1"));
        tabuleiro.colocarPeca(new Torre(new Posicao("a4"), 0), new Posicao("a4"));
        tabuleiro.colocarPeca(new Torre(new Posicao("c1"), 1), new Posicao("c1"));

        List<Posicao> movimentos = torreBranca.movValidos(tabuleiro);

        // Verifica movimentos válidos
        assertTrue(contemPosicao(movimentos, "a2"));
        assertTrue(contemPosicao(movimentos, "a3"));
        assertTrue(contemPosicao(movimentos, "b1"));
        assertTrue(contemPosicao(movimentos, "c1")); // Captura

        // Verifica movimentos inválidos (bloqueados)
        assertFalse(contemPosicao(movimentos, "a5")); // Bloqueado por peça aliada
        assertFalse(contemPosicao(movimentos, "d1")); // Bloqueado após captura
    }

    /**
     * Verifica que a Torre não pode se mover diagonalmente.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testeMovimentoDiagonalInvalido() {
        Torre torre = new Torre(new Posicao("d4"), 0);
        tabuleiro.colocarPeca(torre, new Posicao("d4"));

        // Tentativa de movimento diagonal (inválido)
        tabuleiro.moverPeca(new Posicao("d4"), new Posicao("e5"), Jogador.B);
    }

    // Método auxiliar para verificar se uma lista contém uma posição
    private boolean contemPosicao(List<Posicao> movimentos, String notacao) {
        Posicao alvo = new Posicao(notacao);
        return movimentos.stream().anyMatch(p -> p.equals(alvo));
    }
}