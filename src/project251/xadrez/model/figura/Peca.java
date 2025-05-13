package project251.xadrez.model.figura;

import java.util.ArrayList;
import project251.xadrez.model.tabuleiro.*;

public abstract class Peca {
    protected Posicao posicao;
    public int cor; // 0 pras brancas e 1 pras pretas

    public Peca(Posicao posicao, int cor) {
        this.posicao = posicao;
        this.cor = cor;
    }

    public Posicao getPosicao() {
        return posicao;
    }

    public void setPosicao(Posicao posicao) {
        this.posicao = posicao;
    }
    
    public String getCor() {
        if (cor == 0) {
            return "b";
        } else {
            return "p";
        }
    }


    public void setCor(int cor) {
        this.cor = cor;
    }
    
    public abstract ArrayList<Posicao> movValidos();
}
