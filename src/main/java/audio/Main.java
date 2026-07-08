package main.java.audio;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        WavReader wr = new WavReader("./audio/BlueTit/BlueTit_3.wav");

        SpectrogramGenerator sg = new SpectrogramGenerator();
        SpectrogramAugmentor sa = new SpectrogramAugmentor();
        AudioTools at = new AudioTools();

        sa.createPNG(sg.generateSpectrogram(wr), "./spectrograms/BlueTitNoNoise.png");
        sa.createPNG(sa.addGaussianNoise(sg.generateSpectrogram(wr)), "./spectrograms/BlueTitWithNoise.png");

//        ArrayList<String> paths = new ArrayList<>();
        //at.getAllPaths("./audio", paths);

//        for (String path : paths) {
//            System.out.println(path);
//        }

        //at.convertAllToWav("./audio");
    }
}
