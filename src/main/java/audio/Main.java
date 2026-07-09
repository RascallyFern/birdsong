package main.java.audio;

import java.io.IOException;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) throws IOException {
        WavReader wr = new WavReader("./audio/BlueTit/BlueTit_1.wav");

        SpectrogramGenerator sg = new SpectrogramGenerator();
        SpectrogramAugmentor sa = new SpectrogramAugmentor();
        AudioTools at = new AudioTools();

        ArrayList<String> paths = new ArrayList<>();
        at.getAllPaths("./audio", paths);

        at.groupCSVs("./spectrograms/csv", -1);

//        for (String path : paths) {
//            int count = 1;
//            for (double[][] snippet : sg.splitIntoSongs(sg.generateSpectrogram(new WavReader(path)))) {
//                String dir = path.replace("./audio", "./spectrograms/png").replace(".wav", count + ".png");
//                sa.createPNG(snippet, dir);
//                count++;
//            }
//        }

//        for (String path : paths) {
//            int count = 1;
//            for (double[][] snippet : sg.splitIntoSongs(sg.generateSpectrogram(new WavReader(path)))) {
//                String fileName = path.split("/")[path.split("/").length - 1];
//                String dir = path.replace("./audio", "./spectrograms/csv").replace(fileName, "");
//                sa.createCSV(snippet, dir, fileName.replace(".wav", "~" + count + "~.csv"));
//                count++;
//            }
//
//        }

//        sa.createPNG(sg.generateSpectrogram(wr), "./spectrograms/withoutnoise.png");
//        sa.createPNG(sa.addGaussianNoise(sg.generateSpectrogram(wr)), "./spectrograms/withnoise.png");

    }
}
