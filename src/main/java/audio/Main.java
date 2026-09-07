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

        int shifts, noises;
        String dir;

        at.convertAllToWav("./audio");

//        for (String path : paths) {
//            int count1 = 0;
//
//            if (!(path.contains("BlackHeadedGull"))) {
//                continue;
//            }
//
//            try {
//                at.convertAllToWav(path);
//            } catch (Exception e) {
//                continue;
//            }
//
//            path = path.replace(".mp3", ".wav");
//
//            for (double[][] snippet : sg.splitIntoSongs(sg.generateSpectrogram(new WavReader(path)))) {
//                if (snippet == null) {
//                    continue;
//                }
//                dir = path.replace("\\", "/").replace("./audio", "./spectrograms/pngs-new").replace(".wav", "-" + count1 + ".png");
//                System.out.println(dir);
//                sa.createPNG(snippet, dir);
//                count1++;
//            }
//
//        }
        //at.convertAllToWav("./audio");
//        for (String path : paths) {
//            if (path.contains("BeardedReedling")) {
//                shifts = 5; noises = 4;
//            } else if (path.contains("BlackHeadedGull")) {
//                shifts = 0; noises = 0;
            //}
//         if (path.contains("BlueTit")) {
//                shifts = 1; noises = 0;
//            } else if (path.contains("Bullfinch")) {
//                shifts = 1; noises = 1;
//            } else if (path.contains("CettisWarbler")) {
//                shifts = 1; noises = 2;
//            } else if (path.contains("Cuckoo")) {
//                shifts = 0; noises = 1;
//            } else if (path.contains("Goldcrest")) {
//                shifts = 0; noises = 1;
//            } else if (path.contains("GreatCrestedGrebe")) {
//                shifts = 4; noises = 2;
//            } else if (path.contains("GreatTit")) {
//                shifts = 0; noises = 0;
//            } else if (path.contains("Jackdaw")) {
//                shifts = 1; noises = 1;
//            if (path.contains("Kingfisher")) {
//                shifts = 4; noises = 2;
//            if (path.contains("LittleTern")) {
//                shifts = 4; noises = 1;
//            } else if (path.contains("LongTailedTit")) {
//                shifts = 1; noises = 2;
//             if (path.contains("Magpie")) {
//                shifts = 0; noises = 2;
//            } else if (path.contains("Mallard")) {
//                shifts = 2; noises = 3;
//            } else if (path.contains("noise")) {
//                shifts = 34; noises = 12;
//            } else if (path.contains("Robin")) {
//                shifts = 0; noises = 0;
//            } else if (path.contains("Swift")) {
//                shifts = 1; noises = 1;
//            } else if (path.contains("WillowTit")) {
//                shifts = 2; noises = 1;
//            } else if (path.contains("Yellowhammer")) {
//                shifts = 0; noises = 1;
//            } else {
//                continue;
//            }
//
//
//            path = path.replace(".mp3", ".wav");
//
//            int count1 = 1;
//            for (double[][] snippet : sg.splitIntoSongs(sg.generateSpectrogram(new WavReader(path)))) {
//                if (snippet == null) {
//                    continue;
//                }
//
//                int count2 = 1;
//                for (double[][] augmented : sa.generateAugmented(snippet, shifts, noises)) {
//                    dir = path.replace("\\", "/").replace("./audio", "./spectrograms/csvs-20").replace(".wav", "-" + count1 + "-" + count2 + ".csv");
//                    sa.createCSV(augmented, dir);
//                    count2++;
//                }
//                count1++;
//            }
//        }

        //at.groupCSVs("./spectrograms/csvs-20");

    }
}
