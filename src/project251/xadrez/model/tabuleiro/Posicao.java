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
        char colunaChar = notacao.charAt(0); //coluna = primeiro caractere de notacao
        char linhaChar = notacao.charAt(1); //linha = segundo caractere de notacao

        this.coluna = colunaChar - 'a'; // transforma pra int
        this.linha = 8 - Character.getNumericValue(linhaChar);
    }

    public int getLinha() {
        return linha;
    }

    public int getColuna() {
        return coluna;
    }

    @Override
    public String toString() {
        return "" + (char) ('a' + coluna) + (8 - linha);
    }
}
