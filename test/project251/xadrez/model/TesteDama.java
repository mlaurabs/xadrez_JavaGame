package project251.xadrez.model;

import static org.junit.Assert.*;
import org.junit.Test;


import org.junit.Before;

import java.util.List;

/**
 * Os testes consideram:
 * - Mover-se em qualquer direção (horizontal, vertical e diagonal)
 * - Mover qualquer número de casas
 * - Ser bloqueada por peças da mesma cor
 */
public class TesteDama {

    private Tabuleiro tabuleiro;

    @Before
    public void setup() {
        tabuleiro = new Tabuleiro(); // Tabuleiro vazio
    }

    /**
     * Verifica os movimentos válidos de uma Dama no centro do tabuleiro.
     * Deve incluir todas as posições nas horizontais, verticais e diagonais.
     */
    @Test
    public void testeMovimentosValidos_DamaNoCentro() {
        Posicao posOrigem = new Posicao("d4");
        Dama dama = new Dama(posOrigem, 0); // Dama branca
        tabuleiro.colocarPeca(dama, posOrigem);

        List<Posicao> movimentosValidos = dama.movValidos(tabuleiro);

        // Verifica quantidade de movimentos (27 no centro do tabuleiro)
        assertEquals(27, movimentosValidos.size());

        // Verifica movimentos em todas as direções
        assertTrue(contemPosicao(movimentosValidos, "d8")); // Vertical cima
        assertTrue(contemPosicao(movimentosValidos, "d1")); // Vertical baixo
        assertTrue(contemPosicao(movimentosValidos, "a4")); // Horizontal esquerda
        assertTrue(contemPosicao(movimentosValidos, "h4")); // Horizontal direita
        assertTrue(contemPosicao(movimentosValidos, "a1")); // Diagonal inferior esquerda
        assertTrue(contemPosicao(movimentosValidos, "h8")); // Diagonal superior direita
    }

    /**
     * Verifica que a Dama é bloqueada por peças da mesma cor
     * e pode capturar peças adversárias.
     */
    public void testeMovimentosComBloqueio() {
        Dama damaBranca = new Dama(new Posicao("d1"), 0);
        new Peao(new Posicao("d3"), 0); // Peão aliado
        new Peao(new Posicao("f3"), 1); // Peão adversário

        tabuleiro.colocarPeca(damaBranca, new Posicao("d1"));
        tabuleiro.colocarPeca(new Peao(new Posicao("d3"), 0), new Posicao("d3"));
        tabuleiro.colocarPeca(new Peao(new Posicao("f3"), 1), new Posicao("f3"));

        List<Posicao> movimentos = damaBranca.movValidos(tabuleiro);

        // Verifica movimentos válidos
        assertTrue(contemPosicao(movimentos, "d2")); // Vertical
        assertTrue(contemPosicao(movimentos, "f3")); // Diagonal (captura)
        assertTrue(contemPosicao(movimentos, "a1")); // Horizontal
        assertTrue(contemPosicao(movimentos, "h1")); // Horizontal

        // Verifica movimentos bloqueados
        assertFalse(tabuleiro.moverPeca(new Posicao("d1"), new Posicao("d4"), Jogador.P)); // Bloqueado por peça aliada
        assertFalse(tabuleiro.moverPeca(new Posicao("d1"), new Posicao("h5"), Jogador.P)); // Bloqueado por peça aliada
    }

    /**
     * Verifica que a Dama não pode se mover como cavalo (em L).
     */
    public void testeMovimentoCavaloInvalido() {
        Dama dama = new Dama(new Posicao("d4"), 0);
        tabuleiro.colocarPeca(dama, new Posicao("d4"));

        // Tentativa de movimento em L (inválido)
        assertFalse(tabuleiro.moverPeca(new Posicao("d4"), new Posicao("e6"), Jogador.P));
    }

    /**
     * Verifica que a Dama não pode pular sobre outras peças.
     */
    @Test
    public void testeNaoPodePularPecas() {
        Dama dama = new Dama(new Posicao("a1"), 0);
        new Peao(new Posicao("a2"), 1); // Peão no caminho
        new Peao(new Posicao("b2"), 1); // Peão no caminho diagonal

        tabuleiro.colocarPeca(dama, new Posicao("a1"));
        tabuleiro.colocarPeca(new Peao(new Posicao("a2"), 1), new Posicao("a2"));
        tabuleiro.colocarPeca(new Peao(new Posicao("b2"), 1), new Posicao("b2"));

        List<Posicao> movimentos = dama.movValidos(tabuleiro);

        // Não deve conseguir alcançar casas após os peões
        assertFalse(contemPosicao(movimentos, "a3"));
        assertFalse(contemPosicao(movimentos, "c3"));
        
        // Deve poder capturar os peões adjacentes
        assertTrue(contemPosicao(movimentos, "a2"));
        assertTrue(contemPosicao(movimentos, "b2"));
    }

    /**
     * Verifica os movimentos válidos de uma Dama no canto do tabuleiro.
     */
    @Test
    public void testeMovimentosValidos_DamaNoCanto() {
        Dama dama = new Dama(new Posicao("a1"), 0);
        tabuleiro.colocarPeca(dama, new Posicao("a1"));

        List<Posicao> movimentosValidos = dama.movValidos(tabuleiro);

        // Verifica movimentos específicos
        assertTrue(contemPosicao(movimentosValidos, "a8")); // Vertical
        assertTrue(contemPosicao(movimentosValidos, "h1")); // Horizontal
        assertTrue(contemPosicao(movimentosValidos, "h8")); // Diagonal
        assertFalse(contemPosicao(movimentosValidos, "b3")); // Movimento em L inválido
    }

    // Método auxiliar para verificar se uma lista contém uma posição
    private boolean contemPosicao(List<Posicao> movimentos, String notacao) {
        Posicao alvo = new Posicao(notacao);
        return movimentos.stream().anyMatch(p -> p.equals(alvo));
    }
}