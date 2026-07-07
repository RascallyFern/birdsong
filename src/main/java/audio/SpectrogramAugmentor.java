package main.java.audio;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class SpectrogramAugmentor {

    public void createPNG(double[][] spectrogram, String outputDir) {
        File png = new File(outputDir);
        BufferedImage image = new BufferedImage(spectrogram[0].length, spectrogram.length, BufferedImage.TYPE_INT_RGB);

        for (int y = 0; y < spectrogram.length; y++) {
            for (int x = 0; x < spectrogram[0].length; x++) {
                int mag = (int) (spectrogram[y][x] * 255);
                int rgb = (mag << 16) | (mag << 8) | mag;
                image.setRGB(x, spectrogram.length - y - 1, rgb);
            }
        }

        try {
            if (!png.isDirectory()) {
                png.mkdirs();
            }
            ImageIO.write(image, "PNG", png);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
