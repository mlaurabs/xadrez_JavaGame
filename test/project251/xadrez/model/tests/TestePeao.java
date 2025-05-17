package project251.xadrez.model.tests;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;

import project251.xadrez.model.api.Jogador;
import project251.xadrez.model.figura.Peao;
import project251.xadrez.model.figura.Peca;
import project251.xadrez.model.tabuleiro.Posicao;
import project251.xadrez.model.tabuleiro.Tabuleiro;

import java.util.List;

/**
 * Classe de testes para verificar o comportamento do movimento dos peões no tabuleiro de xadrez.
 * 
 * Os testes são realizados usando o framework JUnit 4 e consideram:
 * - Movimentos válidos no início do jogo
 * - Restrições de movimento após o primeiro lance
 * - Validação de movimentos diagonais
 *  */
public class TestePeao {

    private Tabuleiro tabuleiro;

    /**
     * Configura o ambiente de testes inicializando um tabuleiro com as peças
     * posicionadas em suas posições iniciais, conforme as regras do xadrez.
     */
    @Before
    public void setup() {
        tabuleiro = new Tabuleiro();
        tabuleiro.comecaJogo();
    }

    /**
     * Verifica se o peão branco, posicionado na linha inicial (e2), pode se mover:
     * - Uma casa à frente (e3)
     * - Duas casas à frente (e4)
     *
     * Este teste simula o comportamento do movimento inicial do peão,
     * que permite avançar duas casas se ainda não tiver sido movido.
     */
    @Test
    public void testeMovimentoInicial_umaCasa_ou_duasCasas() {
        Posicao pos_origem = new Posicao(1, 4); // e2
        Peca peca = tabuleiro.getPeca(pos_origem);

        Posicao esperada1 = new Posicao("e3"); // 1 casa à frente
        Posicao esperada2 = new Posicao("e4"); // 2 casas à frente

        List<Posicao> movimentosValidos = peca.movValidos(tabuleiro);

        // Espera-se que o peão tenha exatamente 2 movimentos válidos a partir da posição inicial
        assertEquals(2, movimentosValidos.size());

        // Valida se os movimentos esperados estão presentes na lista retornada
        Posicao destino1 = movimentosValidos.get(0);
        Posicao destino2 = movimentosValidos.get(1);

        assertEquals(esperada1.getLinha(), destino1.getLinha());
        assertEquals(esperada1.getColuna(), destino1.getColuna());

        assertEquals(esperada2.getLinha(), destino2.getLinha());
        assertEquals(esperada2.getColuna(), destino2.getColuna());
    }

    /**
     * Verifica que, após o primeiro movimento,
     * ele não pode mais se mover duas casas. Apenas um avanço de uma casa deve estar disponível.
     */
    @Test
    public void testeMovimentosValidosAposInicio() {
        Jogador jogador = Jogador.B;
        Posicao pos_origem = new Posicao(1, 4); // e2
        Peca peca = tabuleiro.getPeca(pos_origem);

        // Realiza o movimento inicial do peão de e2 para e3
        Posicao destino = peca.movValidos(tabuleiro).get(0); // e3
        tabuleiro.moverPeca(pos_origem, destino, jogador);

        // Recupera a peça já movimentada (agora em e3) e verifica seus novos movimentos válidos
        Posicao novaPosicao = new Posicao("e3");
        Peca peca_pos_move = tabuleiro.getPeca(novaPosicao);
        List<Posicao> movimentos = peca_pos_move.movValidos(tabuleiro);

        // Após o movimento inicial, o peão só pode se mover 1 casa à frente (para e4)
        assertEquals(1, movimentos.size());

        Posicao esperada = new Posicao("e4");
        Posicao resultado = movimentos.get(0);

        assertEquals(esperada.getLinha(), resultado.getLinha());
        assertEquals(esperada.getColuna(), resultado.getColuna());
    }

    /**
     * Verifica que um peão branco não pode realizar um movimento diagonal
     * se não houver uma peça adversária para capturar.
     *
     * Neste caso, o peão tenta mover-se de e2 para f3, o que é inválido,
     * já que f3 está vazia.
     *
     * Espera-se que uma exceção seja lançada para indicar a ilegalidade do movimento.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testeMovimentoDiagonalInvalida() {
        Jogador jogador = Jogador.B;
        Posicao pos_origem = new Posicao("e2");
        Posicao pos_destino = new Posicao("f3"); // casa diagonal vazia

        Peca peca = tabuleiro.getPeca(pos_origem);

        // Deve lançar IllegalArgumentException, pois não há peça a ser capturada em f3
        tabuleiro.moverPeca(pos_origem, pos_destino, jogador);
    }

    /**
     * Verifica que um peão branco pode realizar uma captura válida na diagonal
     * se houver uma peça adversária na casa de destino.
     *
     * O teste insere manualmente um peão preto na posição f3 e tenta mover
     * o peão branco de e2 para f3, o que deve ser permitido como captura.
     */
    @Test
    public void testeMovimentoDiagonalValida() {
        Jogador jogador = Jogador.B;

        // Coloca um peão preto em f3 para ser capturado
        tabuleiro.colocarPeca(new Peao(new Posicao("f3"), 1), new Posicao("f3"));

        Posicao pos_origem = new Posicao("e2");
        Posicao pos_destino = new Posicao("f3");

        Peca peca = tabuleiro.getPeca(pos_origem);

        // O movimento diagonal com captura deve ser permitido
        assertTrue(tabuleiro.moverPeca(pos_origem, pos_destino, jogador));
    }
}
