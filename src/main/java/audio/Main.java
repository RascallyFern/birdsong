package main.java.audio;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        WavReader wr = new WavReader("./audio/BlueTit/BlueTit_1.wav");

        SpectrogramGenerator sg = new SpectrogramGenerator();
        SpectrogramAugmentor sa = new SpectrogramAugmentor();
        AudioTools at = new AudioTools();

        ArrayList<String> paths = new ArrayList<>();
        at.getAllPaths("./audio", paths);

        for (String path : paths) {
            int count = 1;
            for (double[][] snippet : sg.splitIntoSongs(sg.generateSpectrogram(new WavReader(path)))) {
                sa.createPNG(snippet, path.replace("./audio", "./spectrograms").replace(".wav", "snippet" + String.valueOf(count) + ".png"));
                count++;
            }
//
        }

//        sa.createPNG(sg.generateSpectrogram(wr), "./spectrograms/withoutnoise.png");
//        sa.createPNG(sa.addGaussianNoise(sg.generateSpectrogram(wr)), "./spectrograms/withnoise.png");

    }
}
