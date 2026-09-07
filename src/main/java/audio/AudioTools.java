package main.java.audio;

import java.io.*;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Dictionary;

public class AudioTools {

    private ArrayList<String> paths;

    public void convertAllToWav(String path) {
        String lower = path.toLowerCase();

        if (lower.endsWith(".mp3") || lower.endsWith(".wav")) {
            File input = new File(path);
            String basePath = path.substring(0, path.lastIndexOf('.'));

            File temp = new File(basePath + "_temp.wav");
            File output = new File(basePath + ".wav");

            if (temp.exists()) {
                temp.delete();
            }

            File converted = convertToWav(path, temp.getPath());

            if (converted != null && converted.exists()) {
                if (input.delete()) {
                    if (temp.renameTo(output)) {
                        System.out.println("Converted: " + path);
                    } else {
                        System.out.println("Could not rename: " + temp.getPath());
                    }
                } else {
                    System.out.println("Could not delete: " + path);
                    temp.delete();
                }
            }
        } else if (Files.isDirectory(Path.of(path))) {
            try (DirectoryStream<Path> stream = Files.newDirectoryStream(Path.of(path))) {
                for (Path file : stream) {
                    convertAllToWav(file.toString());
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private File convertToWav(String inputPath, String outputPath) {
        ProcessBuilder pb = new ProcessBuilder("ffmpeg", "-y", "-i", inputPath, "-ac", "1", "-ar", "48000", "-c:a", "pcm_s16le", outputPath);

        pb.redirectError(ProcessBuilder.Redirect.INHERIT);

        try {
            Process p = pb.start();
            int exitCode = p.waitFor();

            if (exitCode != 0) {
                System.out.println("FFmpeg failed: " + inputPath);
                return null;
            }

            return new File(outputPath);

        } catch (IOException | InterruptedException e) {
            System.out.println("FFmpeg error: " + e);
            return null;
        }
    }

    public void getAllPaths(String dir, ArrayList<String> pathList) {
        Path path = Path.of(dir);

        if (Files.isRegularFile(path)) {
            String fileName = path.getFileName().toString().toLowerCase();

            if (fileName.endsWith(".wav") ||
                    fileName.endsWith(".mp3") ||
                    fileName.endsWith(".csv")) {

                pathList.add(path.toString());
            }

            return;
        }

        if (!Files.isDirectory(path)) {
            return;
        }

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(path)) {
            for (Path file : stream) {
                getAllPaths(file.toString(), pathList);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void groupCSVs(String dir) throws IOException {
        ArrayList<String> files = new ArrayList<>();
        getAllPaths(dir, files);
        int[] counts = new int[20];

        ArrayList<String> train = new ArrayList<>();
        ArrayList<String> test = new ArrayList<>();

        BufferedWriter bwTrain = new BufferedWriter(new FileWriter(dir + "/grouped-train.csv"));
        BufferedWriter bwTest = new BufferedWriter(new FileWriter(dir + "/grouped-test.csv"));
        BufferedReader br;

        for (String file : files) {
            int index = getLabel(file);
            if (index >= 0) {
                if (counts[index] < 150) {
                    test.add(file);
                    counts[index]++;
                } else {
                    train.add(file);
                }
            }
        }

        Collections.shuffle(train);

        for (String file : train) {
            br = new BufferedReader(new FileReader(file));
            String readLine = br.readLine();
            bwTrain.write(String.valueOf(getLabel(file)) + "\n");
            while (readLine != null) {
                bwTrain.write(readLine + "\n");
                readLine = br.readLine();
            }
        }
        bwTrain.close();

        for (String file : test) {
            br = new BufferedReader(new FileReader(file));
            String readLine = br.readLine();
            bwTest.write(String.valueOf(getLabel(file)) + "\n");
            while (readLine != null) {
                bwTest.write(readLine + "\n");
                readLine = br.readLine();
            }
        }
        bwTest.close();
    }

    public int getLabel(String dir) {
        if (dir.contains("BeardedReedling")) {
            return 0;
        } else if (dir.contains("BlackHeadedGull")) {
            return 1;
        } else if (dir.contains("BlueTit")) {
            return 2;
        } else if (dir.contains("Bullfinch")) {
            return 3;
        } else if (dir.contains("CettisWarbler")) {
            return 4;
        } else if (dir.contains("Cuckoo")) {
            return 5;
        } else if (dir.contains("Goldcrest")) {
            return 6;
        } else if (dir.contains("GreatTit")) {
            return 7;
        } else if (dir.contains("Jackdaw")) {
            return 8;
        }  else if (dir.contains("LittleTern")) {
            return 9;
        } else if (dir.contains("LongTailedTit")) {
            return 10;
        } else if (dir.contains("Magpie")) {
            return 11;
        } else if (dir.contains("Mallard")) {
            return 12;
        } else if (dir.contains("Yellowhammer")) {
            return 13;
        } else if (dir.contains("Robin")) {
            return 14;
        } else if (dir.contains("Swift")) {
            return 15;
        } else if (dir.contains("WillowTit")) {
            return 16;
        } else if (dir.contains("GreatCrestedGrebe")) {
            return 17;
        } else if (dir.contains("Kingfisher")) {
            return 18;
        } else if (dir.contains("noise")) {
            return 19;
        } else {
            System.out.println("Error retrieving label!");
            return -1;
        }
    }

}
