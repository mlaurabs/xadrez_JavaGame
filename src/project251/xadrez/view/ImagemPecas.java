package project251.xadrez.view;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public class ImagemPecas {

    private static final String[] NOMES = {
        "CyanB", "CyanK", "CyanN", "CyanP", "CyanQ", "CyanR",
        "PurpleB", "PurpleK", "PurpleN", "PurpleP", "PurpleQ", "PurpleR"
    };

    private static final Map<String, BufferedImage> mapaImagens = new HashMap<>();

    static {
        for (String nome : NOMES) {
            try {
                BufferedImage img = ImageIO.read(ImagemPecas.class.getResource("/images/" + nome + ".png"));
                mapaImagens.put(nome, img);
            } catch (Exception e) {
                System.err.println("Erro ao carregar imagem: " + nome + " - " + e.getMessage());
            }
        }
    }

    public static BufferedImage getImagem(String nome) {
        return mapaImagens.get(nome);
    }
}
