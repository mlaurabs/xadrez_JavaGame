package project251.xadrez.model.api;

import java.util.HashMap;
import java.util.Map;

/**
 * Representa um jogador no jogo de xadrez, com controle de estado,
 * peças capturadas e informações do jogo.
 */
public enum Jogador {
    B,  // Jogador das peças brancas
    P;  // Jogador das peças pretas

    /** Indica se o jogador está em xeque */
    public boolean emXeque = false;
    
    /** Indica se o jogador está em xeque-mate */
    public boolean xeque_mate = false;

    // Mapa para armazenar as peças capturadas por tipo
    private final Map<String, Integer> pecasCapturadas = new HashMap<>();
    
    // Mapeamento de códigos para nomes completos das peças
    private static final Map<String, String> NOMES_PECAS = Map.of(
        "P", "PEÃO",
        "T", "TORRE",
        "C", "CAVALO",
        "B", "BISPO",
        "D", "DAMA",
        "R", "REI"
    );

    /**
     * Obtém o próximo jogador (alterna entre brancas e pretas).
     * @return Próximo jogador na sequência
     */
    public Jogador proximo() {
        return this == B ? P : B;
    }

    /**
     * Verifica se um tipo de peça é válido.
     * @param tipo (String) da peça a ser validado ex.: "P" de preto
     * @return true se o tipo for válido, false caso contrário
     */
    private boolean isPecaValida(String tipo) {
        return NOMES_PECAS.containsKey(tipo);
    }

    /**
     * Registra uma peça capturada pelo jogador.
     * @param tipo (String) da peça capturada (P, T, C, B, D, R)
     * @throws IllegalArgumentException Se o tipo de peça for inválido
     */
    public void adicionarPecaCapturada(String tipo) {
        if (!isPecaValida(tipo)) {
            throw new IllegalArgumentException("Tipo de peça inválido: " + tipo);
        }
        pecasCapturadas.put(tipo, pecasCapturadas.getOrDefault(tipo, 0) + 1);
    }
    
    /**
     * Imprime o placar formatado com as peças capturadas por ambos os jogadores.
     */
    public static void imprimirPlacarFormatado() {
        System.out.println();
        System.out.println("========================================");
        System.out.println("PLACAR");
        
        // Linha do jogador Preto
        System.out.print("PRETO (P) : ");
        String preto = P.getPecasCapturadasString().replace("P: ", "");
        System.out.println(preto.isEmpty() ? "Nenhuma peça capturada" : formatarPlacar(preto));
        
        // Linha do jogador Branco
        System.out.print("BRANCO (B) : ");
        String branco = B.getPecasCapturadasString().replace("B: ", "");
        System.out.println(branco.isEmpty() ? "Nenhuma peça capturada" : formatarPlacar(branco));
        
        System.out.println("========================================");
    }

    /**
     * Formata a string do placar para plural correto.
     * @param input String com as peças capturadas
     * @return String formatada com plurais corretos
     */
    private static String formatarPlacar(String input) {
        return input.replaceAll("1 TORRE", "1 TORRE")
                  .replaceAll("([2-9]) TORRE", "$1 TORRES")
                  .replaceAll("1 PEÃO", "1 PEÃO")
                  .replaceAll("([2-9]) PEÃO", "$1 PEÕES");
    }
    
    /**
     * Obtém a quantidade de peças capturadas de um tipo específico.
     * @param tipo (Strig) da peça (P, T, C, B, D, R)
     * @return Quantidade de peças capturadas desse tipo
     * @throws IllegalArgumentException Se o tipo for inválido
     */
    public int getPecasCapturadas(String tipo) {
        if (!isPecaValida(tipo)) {
            throw new IllegalArgumentException("Tipo de peça inválido: " + tipo);
        }
        return pecasCapturadas.getOrDefault(tipo, 0);
    }

    /**
     * Retorna uma representação formatada das peças capturadas.
     * @return String no formato "J: X PEÇA, Y PEÇA" (ex: "B: 2 BISPO, 4 PEÃO")
     */
    public String getPecasCapturadasString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this).append(": ");
        
        boolean first = true;
        for (Map.Entry<String, Integer> entry : pecasCapturadas.entrySet()) {
            if (!first) {
                sb.append(", ");
            }
            sb.append(entry.getValue())
              .append(" ")
              .append(NOMES_PECAS.get(entry.getKey()));
            first = false;
        }
        
        return sb.toString();
    }
    
    /**
     * Obtém o nome completo do jogador.
     * @return "BRANCO" ou "PRETO"
     */
    public String getNome() {
        return (this == B) ? "BRANCO" : "PRETO";
    }

    /**
     * Obtém o código numérico da cor do jogador.
     * @return 0 para brancas, 1 para pretas
     */
    public int getCor() {
        return (this == B) ? 0 : 1;
    }
}