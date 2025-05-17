package project251.xadrez.model.api;

public enum Jogador {
    B,
    P;

    public Jogador proximo() {
        return this == B ? P : B;
    }
}
