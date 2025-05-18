package project251.xadrez.model.figura;

import java.util.ArrayList;
import project251.xadrez.model.tabuleiro.*;

public abstract class Peca {
    public Posicao posicao;
    public int cor; 
    public ArrayList<Posicao> movimentos = new ArrayList<Posicao>();
    // 0 pras brancas 
    // 1 pras pretas

    public abstract Peca clonar(Tabuleiro tabuleiro);

    public void setMovimentos(ArrayList<Posicao> movimentos) {
        this.movimentos = movimentos;
    }

    public Peca(Posicao posicao, int cor) {
        this.posicao = posicao;
        this.cor = cor;
        this.movimentos = new ArrayList<Posicao>();
    }

    public Posicao getPosicao() {
        return posicao;
    }

    public void setPosicao(Posicao posicao) {
        this.posicao = posicao;
    }
    
    public String getCor() {
        if (cor == 0) {
            return "B";
        } else {
            return "P";
        }
    }

    public void setCor(int cor) {
        this.cor = cor;
    }
    public abstract String getTipoPeca();
    public abstract ArrayList<Posicao> movValidos(Tabuleiro tabuleiro);
}
