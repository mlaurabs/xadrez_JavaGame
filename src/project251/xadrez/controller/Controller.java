package project251.xadrez.controller;

import javax.swing.*;

import project251.xadrez.model.Jogador;
import project251.xadrez.model.Posicao;
import project251.xadrez.model.TabuleiroObserver;
import project251.xadrez.model.XadrezFacade;
import project251.xadrez.view.PainelTabuleiro;

import java.awt.Component;
import java.awt.Window;
import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class Controller {
    private final XadrezFacade jogo = XadrezFacade.getInstance();
    private Posicao origemSelecionada;
    private ArrayList<Posicao> movimentosValidos = new ArrayList<>();
    public Jogador jogadorAtual = Jogador.C;

    public void registrarObserver(TabuleiroObserver observer) {
        jogo.addObserver(observer);
    }

    public void processarClique(int linha, int coluna) {
        Posicao clicada = new Posicao(linha, coluna);

        if (origemSelecionada == null) {
            // Primeiro clique - seleção da peça
            if (jogo.validarPecaSelecionada(clicada, jogadorAtual)) {
                movimentosValidos = jogo.getMovValidos(jogadorAtual, clicada);
                if (movimentosValidos == null || movimentosValidos.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Essa peça não tem movimentos válidos.\nTente outra peça, jogador " + jogadorAtual + "!");
                    origemSelecionada = null;
                } else {
                    origemSelecionada = clicada;
                    jogo.notificarObservers();
                }
            } else {
                JOptionPane.showMessageDialog(null, "Selecione uma peça válida do jogador " + jogadorAtual + "!");
            }
        } else {
            // Segundo clique - tentativa de movimento
            boolean moveRealizado = false;

            // Verifica se é um movimento válido normal
            if (movimentosValidos.contains(clicada)) { // o erro ta nesses ifs
            	
                moveRealizado = jogo.moverPeca(origemSelecionada, clicada, jogadorAtual);

                // Promoção de peão
                if (moveRealizado && jogo.verificarPromocaoPeao(clicada)) {
                    String[] opcoes = {"Dama", "Torre", "Bispo", "Cavalo"};
                    String escolha = (String) JOptionPane.showInputDialog(null,
                        "Escolha a peça para promover o peão:",
                        "Promoção de Peão",
                        JOptionPane.PLAIN_MESSAGE,
                        null,
                        opcoes,
                        opcoes[0]);

                    if (escolha != null) {
                        jogo.promoverPeao(clicada, escolha);
                    }
                }
            }
            // Verifica tentativa de roque (curto ou longo)
            else if (jogo.isRoquePossivel(clicada)) {
            	System.out.printf("entrei onde eu deveria");
                moveRealizado = jogo.realizarRoque( clicada, origemSelecionada, jogadorAtual);
            }

            // Após movimento (normal ou roque)
            if (moveRealizado) {
                // Verifica xeque e xeque-mate
                if (jogo.estaEmXeque(jogadorAtual.proximo())) {
                    jogo.existeMovimentoQueTiraReiDoXeque(jogadorAtual.proximo());
                    if (jogadorAtual.proximo().xeque_mate) {
                        Component parentComponent = null;
                        JOptionPane optionPane = new JOptionPane(
                            "Xeque-mate! Jogador " + jogadorAtual.proximo() + " perdeu o jogo.",
                            JOptionPane.INFORMATION_MESSAGE);
                        JDialog dialog = optionPane.createDialog("Fim de Jogo");
                        dialog.setModal(true);
                        dialog.setVisible(true);
                        dialog.dispose();

                        voltarParaJanelaInicial(dialog);
                        return;
                    } else {
                        JOptionPane.showMessageDialog(null,
                            "O rei das peças " + jogadorAtual.proximo() + " está em xeque!");
                    }
                }

                jogadorAtual = jogadorAtual.proximo();
            }

            origemSelecionada = null;
            movimentosValidos.clear();
            jogo.notificarObservers();
        }

        Jogador.imprimirPlacarFormatado();
    }


    public void salvarEstadoJogo(File arquivo) {
        try (PrintWriter writer = new PrintWriter(arquivo)) {
            List<String> estado = jogo.exportarEstadoJogo();
            for (String linha : estado) {
                writer.println(linha);
            }
            JOptionPane.showMessageDialog(null, "Jogo salvo com sucesso!");
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro ao salvar: " + ex.getMessage());
        }
    }

    public String[][] getEstadoTabuleiro() {
        return jogo.getEstadoTabuleiro();
    }

    public ArrayList<Posicao> obterMovimentosValidos() {
        return movimentosValidos;
    }

    private void voltarParaJanelaInicial(Component componenteReferencia) {
        if (componenteReferencia instanceof PainelTabuleiro painel) {
            painel.encerrar();
        }

        Window janela = SwingUtilities.getWindowAncestor(componenteReferencia);
        if (janela instanceof JFrame frame) {
            frame.getContentPane().removeAll();
            frame.dispose();
        }

        new project251.xadrez.view.JanelaPrincipal();
    }
}
