package project251.xadrez.model.tests;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;

import project251.xadrez.model.api.Jogador;
import project251.xadrez.model.figura.Bispo;
import project251.xadrez.model.figura.Peca;
import project251.xadrez.model.figura.Peao;
import project251.xadrez.model.tabuleiro.Posicao;
import project251.xadrez.model.tabuleiro.Tabuleiro;

import java.util.List;

/**
 * Os testes consideram: 
 * - Movimentos diagonais ilimitados 
 * - Bloqueio por peças da mesma cor 
 * - Restrição a movimentos não-diagonais
 */
public class TesteBispo {

	private Tabuleiro tabuleiro;

	/**
	 * Configura o ambiente de testes inicializando um tabuleiro vazio.
	 */
	@Before
	public void setup() {
		tabuleiro = new Tabuleiro(); // Tabuleiro vazio
	}

	/**
	 * Verifica os movimentos válidos de um Bispo em um tabuleiro vazio. Deve
	 * incluir todas as posições nas quatro diagonais.
	 */
	@Test
	public void testeMovimentosValidos_BispoEmTabuleiroVazio() {
		Posicao posOrigem = new Posicao("d4");
		Bispo bispo = new Bispo(posOrigem, 0); // Bispo branco
		tabuleiro.colocarPeca(bispo, posOrigem);

		List<Posicao> movimentosValidos = bispo.movValidos(tabuleiro);

		// Verifica quantidade de movimentos (13 no centro do tabuleiro)
		assertEquals(13, movimentosValidos.size());

		// Verifica movimentos nas quatro diagonais
		assertTrue(contemPosicao(movimentosValidos, "a1")); // Diagonal inferior esquerda
		assertTrue(contemPosicao(movimentosValidos, "h8")); // Diagonal superior direita
		assertTrue(contemPosicao(movimentosValidos, "a7")); // Diagonal superior esquerda
		assertTrue(contemPosicao(movimentosValidos, "g1")); // Diagonal inferior direita
	}

	 /**
     * Verifica que o Bispo é bloqueado por peças da mesma cor
     * e pode capturar peças adversárias.
     */
    public void testeMovimentosComBloqueio() {
        // Configuração
        Bispo bispoBranco = new Bispo(new Posicao("c1"), 0);
        new Peao(new Posicao("e3"), 0); // Peão aliado (bloqueio)

        tabuleiro.colocarPeca(bispoBranco, new Posicao("c1"));
        tabuleiro.colocarPeca(new Peao(new Posicao("e3"), 0), new Posicao("e3"));

        List<Posicao> movimentos = bispoBranco.movValidos(tabuleiro);


        assertTrue(contemPosicao(movimentos, "d2")); // pode e movimentar para casas anteriores a peça aliada na sua diagonal
        // Verifica movimentos bloqueados
        assertFalse(tabuleiro.moverPeca(new Posicao("c1"), new Posicao("f4"), Jogador.B)); //bloqueado por peça da mesma cor
    }
	 

	/**
	 * Verifica que o Bispo não pode se mover horizontal ou verticalmente.
	 */
	public void testeMovimentoRetoInvalido() {
		Bispo bispo = new Bispo(new Posicao("c1"), 0);
		tabuleiro.colocarPeca(bispo, new Posicao("c1"));

		// Tentativa de movimento vertical (inválido)
		assertFalse(tabuleiro.moverPeca(new Posicao("c1"), new Posicao("c4"), Jogador.B));
	}

	/**
	 * Verifica que o Bispo não pode pular sobre outras peças.
	 */
	@Test
	public void testeNaoPodePularPecas() {
		Bispo bispo = new Bispo(new Posicao("a1"), 0);
		new Peao(new Posicao("b2"), 1); // Peão no caminho

		tabuleiro.colocarPeca(bispo, new Posicao("a1"));
		tabuleiro.colocarPeca(new Peao(new Posicao("b2"), 1), new Posicao("b2"));

		List<Posicao> movimentos = bispo.movValidos(tabuleiro);

		// Não deve conseguir alcançar casas após o peão
		assertFalse(contemPosicao(movimentos, "c3"));
		assertFalse(contemPosicao(movimentos, "d4"));

		// Deve poder capturar o peão em b2
		assertTrue(contemPosicao(movimentos, "b2"));
	}

	// Método auxiliar para verificar se uma lista contém uma posição
	private boolean contemPosicao(List<Posicao> movimentos, String notacao) {
		Posicao alvo = new Posicao(notacao);
		return movimentos.stream().anyMatch(p -> p.equals(alvo));
	}
}