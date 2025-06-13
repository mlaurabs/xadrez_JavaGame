package project251.xadrez.model;

/**
 * Interface que define um observador do tabuleiro de xadrez.
 * Deve ser implementada por qualquer classe que deseja ser
 * notificada quando o estado do tabuleiro mudar.
 */
public interface TabuleiroObserver {
    void atualizar();  // Método chamado quando o tabuleiro muda
}
