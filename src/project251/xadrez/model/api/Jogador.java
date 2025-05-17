package project251.xadrez.model.api;

import java.util.HashMap;
import java.util.Map;

public enum Jogador {
    B,
    P;

    // Mapa para armazenar as peças capturadas por tipo (usando String)
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

    public Jogador proximo() {
        return this == B ? P : B;
    }

    /**
     * Valida se um tipo de peça é válido
     * param: tipo O código da peça
     * return: true se for válido
     */
    private boolean isPecaValida(String tipo) {
        return NOMES_PECAS.containsKey(tipo);
    }

    /**
     * Adiciona uma peça capturada ao jogador.
     * param: tipo O código da peça capturada (P, T, C, B, D, R)
     * lança IllegalArgumentException se o tipo for inválido
     */
    public void adicionarPecaCapturada(String tipo) {
    	if (!isPecaValida(tipo)) {
            throw new IllegalArgumentException("Tipo de peça inválido: " + tipo);
        }
    	pecasCapturadas.put(tipo, pecasCapturadas.getOrDefault(tipo, 0) + 1);
    }
    
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
     * Formata a string do placar para plural correto
     */
    private static String formatarPlacar(String input) {
        return input.replaceAll("1 TORRE", "1 TORRE")
                  .replaceAll("([2-9]) TORRE", "$1 TORRES")
                  .replaceAll("1 PEÃO", "1 PEÃO")
                  .replaceAll("([2-9]) PEÃO", "$1 PEÕES");
    }
    
    /**
     * Obtém a quantidade de peças capturadas de um determinado tipo.
     * param: tipo O código da peça (P, T, C, B, D, R)
     * return: A quantidade de peças capturadas desse tipo
     * throws: IllegalArgumentException se o tipo for inválido
     */
    public int getPecasCapturadas(String tipo) {
        if (!isPecaValida(tipo)) {
            throw new IllegalArgumentException("Tipo de peça inválido: " + tipo);
        }
        return pecasCapturadas.getOrDefault(tipo, 0);
    }


    /**
     * Retorna uma representação em string das peças capturadas.
     * Exemplo: "B: 2 BISPO, 4 PEÃO"
     * @return String formatada com as peças capturadas
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
    
    public String getNome() {
        if (this == B) {
        	return "BRANCO";
        }else if(this == P) {
        	return "PRETO";
        }
        return null;
    }
}