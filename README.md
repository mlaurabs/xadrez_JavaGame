# Jogo de Xadrez
Neste repositório será desenvolvido o projeto  final da disciplina INF1636,  o qual consiste em um jogo de xadrez em Java.

## Estrutura Inicial do Projeto - atualização 11/05/2025

XadrezProject251
├── src/
│   └── project251/
│       └── xadrez/
│           └── model/
│               ├── api/
│               │   ├── XadrezFacade
│               │   └── Jogador (enum)
│               ├── tabuleiro/
│               │   ├── Tabuleiro
│               │   ├── Posicao
│               │   └── Movimento
│               ├── figura/
│               │   ├── Figura (abstrata)
│               │   ├── Rei herda Figura
│               │   ├── Rainha herda Figura
│               │   ├── Torre herda Figura
│               │   ├── Bispo herda Figura
│               │   ├── Cavalo herda Figura
│               │   └── Peao herda Figura
│               └── excecoes/ -- as classes abaixo ainda não foram criadas
│                   ├── XadrezException
│                   ├── MovimentoInvalidoException
│                   └── PromocaoPeaoException
└── test/
    └── project251/
        └── xadrez/
            └── model/
                └── tests/ -- as classes abaixo ainda não foram criadas
                    ├── TabuleiroTest
                    ├── XadrezFacadeTest
                    ├── PecaTest
                    ├── ReiTest
                    ├── RainhaTest
                    ├── TorreTest
                    ├── BispoTest
                    ├── CavaloTest
                    └── PeaoTest

