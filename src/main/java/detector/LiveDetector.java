package main.java.detector;

import main.java.audio.SpectrogramAugmentor;
import main.java.audio.SpectrogramGenerator;
import main.java.cnn.networks.CNNConv2;
import main.java.cnn.networks.CNNConv3;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class LiveDetector {

    private Microphone mic;
    private CNNConv3 network;
    private SpectrogramGenerator sg;
    private SpectrogramAugmentor sa;
    private int sampleRate;
    private int windowLength;
    private int stepSize;
    private double[] buffer;

    public LiveDetector() throws Exception {
        mic = new Microphone();
        network = new CNNConv3(128, 128);
        sg = new SpectrogramGenerator();
        sa = new SpectrogramAugmentor();
        sampleRate = mic.getSampleRate();
        windowLength = (int) (1.5 * sampleRate);
        stepSize = (int) (0.5 * sampleRate);
        buffer = new double[windowLength];

        loadNetwork("./exportBirdsLargeDecayConv3.csv");
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

            if (collected < windowLength) {
                System.arraycopy(newSamples, 0, buffer, collected, Math.min(newSamples.length, windowLength - collected));

                collected += stepSize;

                if (collected < windowLength) {
                    continue;
                }

                detect(buffer);
            } else {
                System.arraycopy(buffer, stepSize, buffer, 0, windowLength - stepSize);
                System.arraycopy(newSamples, 0, buffer, windowLength - stepSize, stepSize);

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

                int rgb = (value << 16) | (value << 8) | value;

                image.setRGB(x, height - y - 1, rgb);
            }
        }

        ImageIO.write(
                image,
                "PNG",
                new File("./live-spectrogram.png")
        );
    }

    private void detect(double[] samples) {
        if (!containsSound(samples)) {
            System.out.print("\rNoise");
            return;
        }

        double[][] spectrogram = sa.flipSpectrogram(sg.generateSpectrogram(samples, sampleRate));
        double[] predictions = network.forward(spectrogram);
        String prediction = getFullLabel(predictions);

        if (!prediction.equals("Noise")) {
            System.out.println("Prediction: " + prediction);
            System.out.println(Arrays.toString(predictions) + "\n");
        }

        try {
            saveSpectrogram(spectrogram);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    private boolean containsSound(double[] samples) {
        int chunkSize = sampleRate / 10;

        for (int start = 0; start < samples.length; start++) {
            int end = Math.min(start + chunkSize, samples.length);
            double sum = 0;

            for (int i = start; i < end; i++) {
                sum += samples[i] * samples[i];
            }

            double rms = Math.sqrt(sum / (end - start));

            if (rms >= 0.01) {
                return true;
            }
        }

        return false;
    }

    public String getFullLabel(double[] predictions) {
        int label = 0;
        double max = 0;

        for (int i = 0; i < predictions.length; i++) {
            if (predictions[i] > max) {
                max = predictions[i];
                label = i;
            }
        }

        if (max > 0.98) {
            return switch (label) {
                case 0 -> "Bearded Reedling";
                case 1 -> "Black Headed Gull";
                case 2 -> "BlueTit";
                case 3 -> "Bullfinch";
                case 4 -> "Cetti's Warbler";
                case 5 -> "Cuckoo";
                case 6 -> "Goldcrest";
                case 7 -> "GreatTit";
                case 8 -> "Jackdaw";
                case 9 -> "Little Tern";
                case 10 -> "Long Tailed Tit";
                case 11 -> "Magpie";
                case 12 -> "Mallard";
                case 13 -> "Yellowhammer";
                case 14 -> "Robin";
                case 15 -> "Swift";
                case 16 -> "Willow Tit";
                case 17 -> "Great Crested Grebe";
                case 18 -> "Kingfisher";
                case 19 -> "Noise";
                default -> "Error";
            };
        } else {
            return "Noise";
        }
    }

}
