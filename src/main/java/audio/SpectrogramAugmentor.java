package main.java.audio;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;

public class SpectrogramAugmentor {

    private Random r;

    public SpectrogramAugmentor() {
        r = new Random();
    }

    public void createCSV(double[][] spectrogram, String outputDir, String outputName) throws IOException {
        if (spectrogram[0][0] == -1) {
            return;
        }

        File dir = new File(outputDir);
        dir.mkdirs();

        BufferedWriter bw = new BufferedWriter(new FileWriter(outputDir + outputName));

        for (int y = 0; y < spectrogram.length; y++) {
            for (int x = 0; x < spectrogram[0].length; x++) {
                bw.write(spectrogram[y][x] + ",");
            }
            bw.newLine();
        }

        bw.close();
    }

    public void createPNG(double[][] spectrogram, String outputDir) {
        File png = new File(outputDir);

        if (spectrogram[0][0] == -1) {
            return;
        }

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

    public double[][] addGaussianNoise(double[][] spectrogram) {
        double[][] copy = spectrogram.clone();
        for (int i = 0; i < copy.length; i++) {
            for (int j = 0; j < copy[0].length; j++) {
                copy[i][j] = Math.max(0.0, Math.min(1.0, spectrogram[i][j] + r.nextGaussian() * (0.01 + r.nextDouble() * 0.05)));
            }
        }
        return copy;
    }

}
