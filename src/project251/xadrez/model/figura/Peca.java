package project251.xadrez.model.figura;

import project251.xadrez.model.tabuleiro.*;

public abstract class Peca {
    protected Posicao posicao;

    public Peca(Posicao posicao) {
        this.posicao = posicao;
    }

    public Posicao getPosicao() {
        return posicao;
    }

    public void setPosicao(Posicao posicao) {
        this.posicao = posicao;
    }
    
}
