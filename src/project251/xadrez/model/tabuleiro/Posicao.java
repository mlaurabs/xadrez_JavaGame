package project251.xadrez.model.tabuleiro;

import java.util.Objects;

public class Posicao {

    private int linha;
    private int coluna;

    public Posicao(int linha, int coluna) {
        this.linha = linha;
        this.coluna = coluna;
    }

    public Posicao(String notacao) {
        if (notacao == null || notacao.length() != 2) { // se notacao for nulo ou de tamanho diferente de 2
            throw new IllegalArgumentException("Notação inválida: " + notacao); // retorna um erro
        }
        char colunaChar = notacao.charAt(0); // coluna = primeiro caractere de notacao
        char linhaChar = notacao.charAt(1);  // linha = segundo caractere de notacao

        this.coluna = colunaChar - 'a'; // transforma pra int, de 'a' a 'h'
        this.linha = Character.getNumericValue(linhaChar) - 1; // transforma para 0-based (linha 1 -> 0, linha 2 -> 1, etc.)
    }
    
    public String notacao() {
    	char colunaChar = (char) ('a' + this.coluna); // Converte coluna (0-7) para 'a'-'h'
        char linhaChar = (char) ('1' + this.linha);   // Converte linha (0-7) para '1'-'8'
        return String.valueOf(colunaChar) + linhaChar;
    }

    
    public int getLinha() {
        return linha;
    }

    public int getColuna() {
        return coluna;
    }

     // Sobrescreve equals() para comparar se duas Posicoes são iguais
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true; // Se for a mesma referência, são iguais
        if (obj == null || getClass() != obj.getClass()) return false; // Se for null ou de classe diferente, são diferentes

        Posicao outraPosicao = (Posicao) obj; // Cast seguro após verificação do getClass()
        return this.coluna == outraPosicao.coluna && this.linha == outraPosicao.linha; // Compara coluna e linha
    }

    // Sobrescreve hashCode() para garantir consistência com equals()
    @Override
    public int hashCode() {
        return Objects.hash(coluna, linha); // Gera um código hash baseado em coluna e linha
    }
    
    @Override
    public String toString() {
        return "" + (char) ('a' + coluna) + (linha + 1); // converte de volta para notação de xadrez
    }
}
