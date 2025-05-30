package project251.xadrez.model.tabuleiro;

import java.util.Objects;

/**
 * Representa uma posição no tabuleiro de xadrez usando coordenadas.
 * Permite conversão entre notação algébrica (ex: "a1") e coordenadas numéricas (linha, coluna).
 */

public class Posicao {

    private int linha;
    private int coluna;

    /**
     * Cria uma posição com coordenadas numéricas.
     * @param linha Número da linha (0 a 7, onde 0 é a primeira linha)
     * @param coluna Número da coluna (0 a 7, onde 0 é a coluna 'a')
     */
    public Posicao(int linha, int coluna) {
        this.linha = linha;
        this.coluna = coluna;
    }

    /**
     * Cria uma posição a partir de notação algébrica de xadrez.
     * @param notacao (String) no formato letra+numero (ex: "e4")
     * @throws IllegalArgumentException Se a notação for inválida
     */
    public Posicao(String notacao) {
        if (notacao == null || notacao.length() != 2) { // se notacao for nulo ou de tamanho diferente de 2
            throw new IllegalArgumentException("Notação inválida: " + notacao); // retorna um erro
        }
        char colunaChar = notacao.charAt(0); // coluna = primeiro caractere de notacao
        char linhaChar = notacao.charAt(1);  // linha = segundo caractere de notacao

        this.coluna = colunaChar - 'a'; // transforma pra int, de 'a' a 'h'
        this.linha = Character.getNumericValue(linhaChar) - 1; // transforma para 0-based (linha 1 -> 0, linha 2 -> 1, etc.)
    }
    
    
    /**
     * Retorna a notação algébrica desta posição.
     * @return String no formato letra+numero (ex: "a1")
     */
    public String notacao() {
    	char colunaChar = (char) ('a' + this.coluna); // Converte coluna (0-7) para 'a'-'h'
        char linhaChar = (char) ('1' + this.linha);   // Converte linha (0-7) para '1'-'8'
        return String.valueOf(colunaChar) + linhaChar;
    }

    /**
     * Obtém o número da linha.
     * @return Valor de 0 a 7 (0 = primeira linha)
     */
    public int getLinha() {
        return linha;
    }

    /**
     * Obtém o número da coluna.
     * @return Valor de 0 a 7 (0 = coluna 'a')
     */
    public int getColuna() {
        return coluna;
    }

    /**
     * Compara esta posição com outro objeto.
     * @param obj Objeto a ser comparado
     * @return true se as posições forem iguais (mesma linha e coluna)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true; // Se for a mesma referência, são iguais
        if (obj == null || getClass() != obj.getClass()) return false; // Se for null ou de classe diferente, são diferentes

        Posicao outraPosicao = (Posicao) obj; // Cast seguro após verificação do getClass()
        return this.coluna == outraPosicao.coluna && this.linha == outraPosicao.linha; // Compara coluna e linha
    }

    /**
     * Gera um código hash para esta posição.
     * @return Código hash baseado nas coordenadas
     */
    @Override
    public int hashCode() {
        return Objects.hash(coluna, linha); // Gera um código hash baseado em coluna e linha
    }
    
    /**
     * Retorna a representação em string da posição.
     * @return Notação algébrica (ex: "e4")
     */
    @Override
    public String toString() {
        return "" + (char) ('a' + coluna) + (linha + 1); // converte de volta para notação de xadrez
    }
}
