package main.java.cnn;
import main.java.audio.AudioTools;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class Main {
    public static void main(String[] args) throws IOException {
        Dataset train = new Dataset("./spectrograms/csvs48000hz/grouped-train.csv");
        Dataset test = new Dataset("./spectrograms/csvs48000hz/grouped-test.csv");

        double[][][] trainImages = train.getData();
        double[][][] testImages = test.getData();

        int[] trainLabels = train.getLabel();
        int[] testLabels = test.getLabel();

        CNN network = new CNN(128, 128);
        System.out.println(Arrays.toString(trainLabels));

        network.setData(trainImages, testImages);
        network.setLabels(trainLabels, testLabels);

        network.train(8);
        network.exportToCSV();

        try {
            network.importFromCSV("./exportBirdTest.csv");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        System.out.println(Arrays.toString(network.forward(CNN.pngToArray("./live-spectrogram.png"))));

        AudioTools at = new AudioTools();
        ArrayList<String> paths = new ArrayList<>();
        at.getAllPaths("./spectrograms/csvs", paths);
        Collections.shuffle(paths);

        int count = 0;
        for (String path : paths) {
            if (!path.contains("grouped.csv") && count < 100) {
                System.out.println(path + ": ");
                network.forwardCSV(path);
                count++;
            } else if (count == 100) {
                return;
            }
        }
    }
}