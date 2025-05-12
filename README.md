# Jogo de Xadrez
Neste repositório será desenvolvido o projeto  final da disciplina INF1636,  o qual consiste em um jogo de xadrez em Java.

## Estrutura Inicial do Projeto - atualização 11/05/2025

# Estrutura do Projeto Xadrez

```mermaid
treeDiagram
    root(XadrezProject251):::root
        src(src/):::folder
            project251(project251/):::folder
                xadrez(xadrez/):::folder
                    model(model/):::folder
                        api(api/):::folder
                            XadrezFacade:::class
                            Jogador:::enum
                        tabuleiro(tabuleiro/):::folder
                            Tabuleiro:::class
                            Posicao:::class
                            Movimento:::class
                        figura(figura/):::folder
                            Figura(Figura):::abstract
                            Rei(Rei):::class
                            Rainha(Rainha):::class
                            Torre(Torre):::class
                            Bispo(Bispo):::class
                            Cavalo(Cavalo):::class
                            Peao(Peao):::class
                        excecoes(excecoes/):::folder
                            XadrezException:::exception
                            MovimentoInvalidoException:::exception
                            PromocaoPeaoException:::exception
        test(test/):::folder
            project251(project251/):::folder
                xadrez(xadrez/):::folder
                    model(model/):::folder
                        tests(tests/):::folder
                            TabuleiroTest:::test
                            XadrezFacadeTest:::test
                            PecaTest:::test
                            ReiTest:::test
                            RainhaTest:::test
                            TorreTest:::test
                            BispoTest:::test
                            CavaloTest:::test
                            PeaoTest:::test

    classDef root fill:#2ecc71,stroke:#27ae60,color:white
    classDef folder fill:#3498db,stroke:#2980b9,color:white
    classDef class fill:#f1c40f,stroke:#f39c12
    classDef enum fill:#9b59b6,stroke:#8e44ad,color:white
    classDef abstract fill:#e74c3c,stroke:#c0392b,color:white
    classDef exception fill:#e67e22,stroke:#d35400
    classDef test fill:#2ecc71,stroke:#27ae60

    %% Relacionamentos de herança
    Rei --> Figura
    Rainha --> Figura
    Torre --> Figura
    Bispo --> Figura
    Cavalo --> Figura
    Peao --> Figura
    
    %% Relacionamentos de exceção
    MovimentoInvalidoException --> XadrezException
    PromocaoPeaoException --> XadrezException

