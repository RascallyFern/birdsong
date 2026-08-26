package main.java.detector;

import main.java.audio.SpectrogramGenerator;
import main.java.cnn.CNN;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class LiveDetector {

    private Microphone mic;
    private CNN network;
    private SpectrogramGenerator sg;
    private int sampleRate;
    private int windowLength;
    private int stepSize;
    private double[] buffer;

    public LiveDetector() throws Exception {
        mic = new Microphone();
        network = new CNN(128, 128);
        sg = new SpectrogramGenerator();
        sampleRate = mic.getSampleRate();
        windowLength = (int) (1.5 * sampleRate);
        stepSize = (int) (0.5 * sampleRate);
        buffer = new double[windowLength];

        loadNetwork("./exportBirdTest.csv");
    }

    private void loadNetwork(String exportDir) {
        try {
            network.importFromCSV(exportDir);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void start() {
        mic.start();
        System.out.println("Listening....");

        int collected = 0;

        while (true) {
            double[] newSamples = mic.record(stepSize);

            System.arraycopy(buffer, stepSize, buffer, 0, windowLength - stepSize);
            System.arraycopy(newSamples, 0, buffer, windowLength - stepSize, stepSize);

            collected += stepSize;

            if (collected >= windowLength) {
                detect(buffer);
            }
        }
    }

    private void saveSpectrogram(double[][] spectrogram) throws Exception {

        int width = spectrogram[0].length;
        int height = spectrogram.length;

        BufferedImage image =
                new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {

                int value = (int) (spectrogram[y][x] * 255);

                value = Math.max(0, Math.min(255, value));

                int rgb =
                        (value << 16) |
                                (value << 8) |
                                value;

                image.setRGB(
                        x,
                        height - y - 1,
                        rgb
                );
            }
        }

        ImageIO.write(
                image,
                "PNG",
                new File("./live-spectrogram.png")
        );
    }

    private void detect(double[] samples) {
        double[][] spectrogram = sg.generateSpectrogram(samples, sampleRate);
        getFullLabel(network.forward(spectrogram));
        try {
            saveSpectrogram(spectrogram);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    private double getRMS(double[] samples) {
        double sum = 0;

        for (double sample : samples) {
            sum += sample * sample;
        }

        return Math.sqrt(sum / samples.length);
    }

    private void getFullLabel(double[] predictions) {
        int label = 0;
        double max = 0;

        for (int i = 0; i < predictions.length; i++) {
            if (predictions[i] > max) {
                max = predictions[i];
                label = i;
            }
        }


        switch (label) {
            case 6:
                System.out.println("Blue Tit");
                break;
            case 0:
                System.out.println("Bullfinch");
                break;
            case 1:
                System.out.println("Cetti's Warbler");
                break;
            case 2:
                System.out.println("Cuckoo");
                break;
            case 3:
                System.out.println("Goldcrest");
                break;
            case 4:
                System.out.println("Great Tit");
                break;
            case 5:
                System.out.println("Noise");
                break;
            default:
                break;
        }
    }

}
