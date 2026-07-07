package main.java.audio;

public class Main {
    public static void main(String[] args) {
        WavReader wr = new WavReader("./audio/BlueTit/BlueTit_1.wav");
        SpectrogramGenerator sg = new SpectrogramGenerator();
        SpectrogramAugmentor sa = new SpectrogramAugmentor();

        sa.createPNG(sg.generateSpectrogram(wr), "./spectrograms/test2.png");
    }
}
