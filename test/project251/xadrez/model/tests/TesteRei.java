package project251.xadrez.model.tests;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.ExpectedException;

import project251.xadrez.model.api.Jogador;
import project251.xadrez.model.figura.Rei;
import project251.xadrez.model.figura.Torre;
import project251.xadrez.model.figura.Peao;
import project251.xadrez.model.tabuleiro.Posicao;
import project251.xadrez.model.tabuleiro.Tabuleiro;

import java.util.List;

/**
 * Os testes consideram: - Movimento de uma casa em qualquer direção -
 * Verificação de xeque - Movimentos inválidos
 */
public class TesteRei {

	private Tabuleiro tabuleiro;

	@Rule
	public ExpectedException exceptionRule = ExpectedException.none();

	@Before
	public void setup() {
		tabuleiro = new Tabuleiro(); // Tabuleiro vazio
	}

	/**
	 * Verifica os movimentos válidos de um Rei no centro do tabuleiro.
	 */
	@Test
	public void testeMovimentosValidos_ReiNoCentro() {
		Rei rei = new Rei(new Posicao("e4"), 0); // Rei branco
		tabuleiro.colocarPeca(rei, new Posicao("e4"));

		List<Posicao> movimentosValidos = rei.movValidos(tabuleiro);

		// Deve ter 8 movimentos possíveis no centro
		assertEquals(8, movimentosValidos.size());

		// Verifica todas as direções
		assertTrue(contemPosicao(movimentosValidos, "e3")); // Baixo
		assertTrue(contemPosicao(movimentosValidos, "e5")); // Cima
		assertTrue(contemPosicao(movimentosValidos, "d4")); // Esquerda
		assertTrue(contemPosicao(movimentosValidos, "f4")); // Direita
		assertTrue(contemPosicao(movimentosValidos, "d3")); // Diagonal inferior esquerda
		assertTrue(contemPosicao(movimentosValidos, "f5")); // Diagonal superior direita
	}

	/**
	 * Verifica que o Rei não pode se mover para casas controladas pelo oponente.
	 *//*
		 * @Test public void testeMovimentosComXeque() { Rei reiBranco = new Rei(new
		 * Posicao("e1"), 0); new Torre(new Posicao("e8"), 1); // Torre preta ameaçando
		 * 
		 * tabuleiro.colocarPeca(reiBranco, new Posicao("e1"));
		 * tabuleiro.colocarPeca(new Torre(new Posicao("e8"), 1), new Posicao("e8"));
		 * 
		 * List<Posicao> movimentos = reiBranco.movValidos(tabuleiro);
		 * 
		 * // Não pode mover para e2 (sob ataque da torre)
		 * assertFalse(contemPosicao(movimentos, "e2"));
		 * 
		 * // Movimentos válidos mesmo em xeque assertTrue(contemPosicao(movimentos,
		 * "f1")); assertTrue(contemPosicao(movimentos, "f2")); }

	/**
	 * Verifica movimento inválido de duas casas.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testeMovimentoInvalido_ReiNaoPodeMoverDuasCasas() {
		Rei rei = new Rei(new Posicao("e1"), 0);
		tabuleiro.colocarPeca(rei, new Posicao("e1"));

		tabuleiro.moverPeca(new Posicao("e1"), new Posicao("e3"), Jogador.B);
	}


	// Método auxiliar para verificar se uma lista contém uma posição
	private boolean contemPosicao(List<Posicao> movimentos, String notacao) {
		Posicao alvo = new Posicao(notacao);
		return movimentos.stream().anyMatch(p -> p.equals(alvo));
	}
}