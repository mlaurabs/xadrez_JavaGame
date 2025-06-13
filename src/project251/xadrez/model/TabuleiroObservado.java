package project251.xadrez.model;

import java.util.List;

/**
 * Interface para objetos que podem ser observados por TabuleiroObserver.
 * Define métodos para registrar, remover e notificar observadores.
 */
public interface TabuleiroObservado {

    /**
     * Registra um novo observador.
     * @param observer o observador a ser adicionado
     */
    void addObserver(TabuleiroObserver observer);

    /**
     * Remove um observador previamente registrado.
     * @param observer o observador a ser removido
     */
    void removeObserver(TabuleiroObserver observer);

    /**
     * Notifica todos os observadores registrados sobre uma mudança de estado.
     */
    void notificarObservers();
}
