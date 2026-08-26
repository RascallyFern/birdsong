package main.java.audio;

import java.io.IOException;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) throws IOException {
        SpectrogramGenerator sg = new SpectrogramGenerator();
        SpectrogramAugmentor sa = new SpectrogramAugmentor();
        AudioTools at = new AudioTools();

        ArrayList<String> paths = new ArrayList<>();
        at.getAllPaths("./audio", paths);

        //at.convertAllToWav("./audio");

        //at.groupCSVs("./spectrograms/csvs48000hz", -1);


        int shifts, noises;

        String dir;
        for (String path : paths) {
            if (path.contains("BlueTit")) {
                shifts = 1; noises = 1;
            } else if (path.contains("Bullfinch")) {
                shifts = 3; noises = 3;
            } else if (path.contains("CettisWarbler") || path.contains("Goldcrest")) {
                shifts = 2; noises = 3;
            } else if (path.contains("Cuckoo")) {
                shifts = 1; noises = 2;
            } else if (path.contains("GreatTit")) {
                shifts = 1;
                noises = 1;
            } else if (path.contains("noise")) {
                shifts = 1;
                noises = 1;
            } else if (path.contains("Bearded")){
                continue;
            } else {
                continue;
            }

            int count1 = 1;
            for (double[][] snippet : sg.splitIntoSongs(sg.generateSpectrogram(new WavReader(path)))) {
                if (snippet == null) {
                    continue;
                }

                int count2 = 1;
                for (double[][] augmented : sa.generateAugmented(snippet, shifts, noises)) {
                    dir = path.replace("./audio", "./spectrograms/csvs48000hz").replace(".wav", "-" + count1 + "-" + count2 + ".csv");
                    sa.createCSV(augmented, dir);
                    count2++;
                }
                count1++;
            }
        }

        at.groupCSVs("./spectrograms/csvs48000hz", -1);

    }
}
