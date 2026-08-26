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

    public void createCSV(double[][] spectrogram, String outputDir) throws IOException {
        if (spectrogram == null) {
            return;
        }

        String[] split = outputDir.split("/");
        File csv = new File(outputDir.replace(split[split.length - 1], ""));
        csv.mkdirs();
        BufferedWriter bw = new BufferedWriter(new FileWriter(outputDir));

        for (int y = 0; y < spectrogram.length; y++) {
            for (int x = 0; x < spectrogram[0].length; x++) {
                bw.write(String.valueOf(spectrogram[spectrogram.length - y - 1][x]));

                if (x < spectrogram[0].length - 1) {
                    bw.write(",");
                }
            }
            bw.newLine();
        }

        bw.close();
        System.out.println("[" + outputDir + "] saved successfully!");
    }

    public boolean createPNG(double[][] spectrogram, String outputDir) {
        if (spectrogram == null) {
            return false;
        }

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
            System.out.println("[" + outputDir + "] saved successfully!");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return true;
    }

    private double[][] addGaussianNoise(double[][] spectrogram) {
        if (spectrogram == null) {
            return null;
        }

        double[][] copy = new double[spectrogram.length][spectrogram[0].length];

        for (int i = 0; i < spectrogram.length; i++) {
            copy[i] = spectrogram[i].clone();
        }

        for (int i = 0; i < copy.length; i++) {
            for (int j = 0; j < copy[0].length; j++) {
                copy[i][j] = Math.max(0.0, Math.min(1.0, spectrogram[i][j] + r.nextGaussian() * (0.01 + r.nextDouble() * 0.05)));
            }
        }

        return copy;
    }

    private double[][] shiftX(double[][] spectrogram) {
        if (spectrogram == null) {
            return null;
        }

        double[][] shifted = new double[spectrogram.length][spectrogram[0].length];

        int shift = r.nextInt(20, 50);
        int width = spectrogram[0].length;
        int length = width - shift;

        if (r.nextInt(0, 2) == 0) {
            //shift left
            for (int row = 0; row < spectrogram.length; row++) {
                System.arraycopy(spectrogram[row], shift, shifted[row], 0, length);
            }
        } else {
            //shift right
            for (int row = 0; row < spectrogram.length; row++) {
                System.arraycopy(spectrogram[row], 0, shifted[row], shift, length);
            }
        }

        return shifted;
    }

    public double[][][] generateAugmented(double[][] spectrogram, int shifts, int noises) {
        if (spectrogram == null) {
            return null;
        }

        //output will have original, only shifted, only with noise and shifted with noise
        int outputs = (shifts * 2) + noises + 1;
        int index = 1;
        double[][][] augmented = new double[outputs][spectrogram.length][spectrogram[0].length];
        augmented[0] = spectrogram;

        for (int s = 0; s < shifts; s++) {
            augmented[index] = shiftX(spectrogram);
            index++;
        }

        for (int s = 0; s < shifts; s++) {
            augmented[index] = addGaussianNoise((shiftX(spectrogram)));
            index++;
        }

        for (int n = 0; n < noises; n++) {
            augmented[index] = addGaussianNoise(spectrogram);
            index++;
        }

        return augmented;
    }
}
