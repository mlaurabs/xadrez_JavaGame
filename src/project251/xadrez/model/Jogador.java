package project251.xadrez.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Representa um jogador no jogo de xadrez, com controle de estado,
 * peças capturadas e informações do jogo.
 */
public enum Jogador {
    C,  // Jogador das peças do time 1 (cyon)
    P;  // Jogador das peças do time 2 (purple)

    /** Indica se o jogador está em xeque */
    public boolean emXeque = false;
    
    /** Indica se o jogador está em xeque-mate */
    public boolean xeque_mate = false;
    
 // Campos para controle de roque
    public boolean reiMoveu = false;
    public boolean torreEsquerdaMoveu = false;
    public boolean torreDireitaMoveu = false;


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
     * Verifica se um tipo de peça é válido.
     * @param tipo (String) da peça a ser validado ex.: "P" de preto
     * @return true se o tipo for válido, false caso contrário
     */
    private boolean isPecaValida(String tipo) {
        return NOMES_PECAS.containsKey(tipo);
    }
    
    /**
     * Obtém o próximo jogador (alterna entre cyons e Purples).
     * @return Próximo jogador na sequência
     */
    public Jogador proximo() {
        return this == C ? P : C;
    }
    
    public Jogador atual() {
        return this;
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
     * Reinicia o placar, limpando as peças capturadas de ambos os jogadores.
     */
    public static void reiniciarPartida() {
        C.pecasCapturadas.clear();
        P.pecasCapturadas.clear();
        C.emXeque = false;
        P.emXeque = false;
        C.xeque_mate = false;
        P.xeque_mate = false;
        C.reiMoveu = false;
        P.reiMoveu = false;
        C.torreEsquerdaMoveu = false;
        P.torreEsquerdaMoveu = false;
        C.torreDireitaMoveu = false;
        P.torreDireitaMoveu = false;
    }

    
    /**
     * Imprime o placar formatado com as peças capturadas por ambos os jogadores.
     */
    public static void imprimirPlacarFormatado() {
        System.out.println();
        System.out.println("========================================");
        System.out.println("PLACAR");
        
        // Linha do jogador Preto
        System.out.print("Cyon (C) : ");
        String cyon = C.getPecasCapturadasString().replace("C: ", "");
        System.out.println(cyon.isEmpty() ? "Nenhuma peça capturada" : formatarPlacar(cyon));
        
        // Linha do jogador Branco
        System.out.print("Purple (P) : ");
        String purple = P.getPecasCapturadasString().replace("P: ", "");
        System.out.println(purple.isEmpty() ? "Nenhuma peça capturada" : formatarPlacar(purple));
        
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
    
    public Map<String, Integer> getPecasCapturadas() {
        return new HashMap<>(pecasCapturadas);
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
     * @return "PURPLE" ou "CYON"
     */
    public String getNome() {
        return (this == C) ? "CYON" : "PURPLE";
    }

    /**
     * Obtém o código numérico da cor do jogador.
     * @return 0 para CYON, 1 para PURPLE
     */
    public int getCor() {
        return (this == C) ? 0 : 1;
    }
    
    public int getLinhaInicial() {
    	return (this == C) ? 7 : 0;
    }
}