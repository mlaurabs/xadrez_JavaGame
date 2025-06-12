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
            // Primeiro clique - tentativa de selecionar peça
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
            // Segundo clique - tentativa de mover a peça
            if (movimentosValidos.contains(clicada)) {
                if (jogo.moverPeca(origemSelecionada, clicada, jogadorAtual)) {
                    if (jogo.verificarPromocaoPeao(clicada)) {
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

                    jogadorAtual = jogadorAtual.proximo();
                    jogo.verificarXeque(jogadorAtual);

                    if (jogadorAtual.emXeque == true) {
                    	JOptionPane optionPane = new JOptionPane(
                    	    "O rei do Jogador " + jogadorAtual.proximo() + " está em Xeque.",
                    	    JOptionPane.INFORMATION_MESSAGE);
                    	JDialog dialog = optionPane.createDialog("Xeque");
                    	dialog.setModal(true);
                    	dialog.setVisible(true);
                    	dialog.dispose();
                    }
                }
            }
            origemSelecionada = null;
            if (movimentosValidos != null && !movimentosValidos.isEmpty()) {
                movimentosValidos.clear();
            }
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
        // Verifica se o componente é um PainelTabuleiro e o encerra explicitamente
        if (componenteReferencia instanceof PainelTabuleiro painel) {
            painel.encerrar(); // Método que você deve implementar no PainelTabuleiro
        }

        // Fecha a janela principal onde o componente está
        Window janela = SwingUtilities.getWindowAncestor(componenteReferencia);
        if (janela instanceof JFrame frame) {
            frame.getContentPane().removeAll(); // Limpa os componentes
            frame.dispose(); // Fecha a janela completamente
        }

        // Reinicia o jogo ou volta à tela principal
        new project251.xadrez.view.JanelaPrincipal();
    }

}
