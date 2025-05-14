package project251.xadrez.model.tabuleiro;

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

    public int getLinha() {
        return linha;
    }

    public int getColuna() {
        return coluna;
    }

    @Override
    public String toString() {
        return "" + (char) ('a' + coluna) + (linha + 1); // converte de volta para notação de xadrez
    }
}
