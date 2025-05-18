package project251.xadrez.model.tests;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.ExpectedException;

import project251.xadrez.model.api.Jogador;
import project251.xadrez.model.figura.*;
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
	public void testeMovimentoInvalido_ReiNaoPodeMoverDuasCasas() {
		Rei rei = new Rei(new Posicao("e1"), 0);
		tabuleiro.colocarPeca(rei, new Posicao("e1"));

		assertFalse(tabuleiro.moverPeca(new Posicao("e1"), new Posicao("e3"), Jogador.B));
	}
	
	@Test
	public void testeXequeNormal() {
	    Jogador jogador_p = Jogador.P;
	    
	    Torre torre = new Torre(new Posicao("d7"), 1);
	    tabuleiro.colocarPeca(torre, new Posicao("d7"));
	    
	    Rei rei = new Rei(new Posicao("e1"), 0); 
	    tabuleiro.colocarPeca(rei, new Posicao("e1"));
	    
	    tabuleiro.moverPeca(new Posicao("d7"), new Posicao("e7"), jogador_p);
	    
	    assertFalse(rei.estaEmXeque()); 
	    
	    rei.verificaXeque(tabuleiro);

	    assertTrue(rei.estaEmXeque()); 
	}
	
	@Test
	public void testeXequeDescoberto() {
	    Tabuleiro tabuleiro = new Tabuleiro();
	    Jogador jogador_b = Jogador.B;

	    // Rei preto em e8
	    Rei reiPreto = new Rei(new Posicao("e8"), 1);
	    tabuleiro.colocarPeca(reiPreto, new Posicao("e8"));

	    // Torre branca em e3 (vai causar o xeque depois)
	    Torre torreBranca = new Torre(new Posicao("e3"), 0);
	    tabuleiro.colocarPeca(torreBranca, new Posicao("e3"));

	    // Bispo branco em e2 (bloqueando a torre)
	    Bispo bispoBranco = new Bispo(new Posicao("e2"), 0);
	    tabuleiro.colocarPeca(bispoBranco, new Posicao("e2"));

	    // Movimento do bispo para liberar a torre (movimento qualquer, por exemplo para d3)
	    tabuleiro.moverPeca(new Posicao("e2"), new Posicao("d3"), jogador_b);
	    
	    assertFalse(reiPreto.estaEmXeque()); 

	    // Verificar se o rei preto está em xeque após o xeque descoberto
	    reiPreto.verificaXeque(tabuleiro);

	    assertTrue(reiPreto.estaEmXeque());
	}


	// Método auxiliar para verificar se uma lista contém uma posição
	private boolean contemPosicao(List<Posicao> movimentos, String notacao) {
		Posicao alvo = new Posicao(notacao);
		return movimentos.stream().anyMatch(p -> p.equals(alvo));
	}
}