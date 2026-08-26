package main.java.audio;

import java.io.*;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Dictionary;

public class AudioTools {

    private ArrayList<String> paths;

    public void convertAllToWav(String path) {
        if (path.toLowerCase().contains(".mp3")) {
            File temp = new File(path.substring(0, path.length() - 3) + "wav");
            temp.delete();
            convertToWav(path);
            File original = new File(path);
            if (original.delete()) {
                System.out.println("Deleted: " + path);
                System.out.println("Created: " + temp.getName());
            }
        } else if (!path.contains(".wav")) {
            Path dir = Path.of(path);

            try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir)) {
                for (Path file : stream) {
                    convertAllToWav(path + "/" + file.getFileName());
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private File convertToWav(String mp3Dir) {
        String wavDir = mp3Dir.substring(0, mp3Dir.length() - 4) + ".wav";
        ProcessBuilder pb = new ProcessBuilder("ffmpeg", "-i", mp3Dir, "-acodec", "pcm_s16le", wavDir);
        try {
            Process p = pb.start();
            p.waitFor();
        } catch (IOException | InterruptedException e) {
            System.out.println(e);
            return null;
        }

        return new File(wavDir);
    }

    public void getAllPaths(String dir, ArrayList<String> pathList) {
        if (dir.contains(".wav") | dir.contains(".mp3") | dir.contains(".csv")) {
            pathList.add(dir);
            return;
        }

        Path p = Path.of(dir);
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(p)) {
            for (Path file : stream) {
                getAllPaths(dir + "/" + file.getFileName(), pathList);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void groupCSVs(String dir, int fileLimit) throws IOException {
        ArrayList<String> files = new ArrayList<>();
        getAllPaths(dir, files);
        Collections.shuffle(files);

        ArrayList<String> test = new ArrayList(files.subList(0, (int) (files.size() * 0.1)));
        ArrayList<String> train = new ArrayList(files.subList((int) (files.size() * 0.1), files.size()));

        BufferedWriter bwTrain = new BufferedWriter(new FileWriter(dir + "/grouped-train.csv"));
        BufferedWriter bwTest = new BufferedWriter(new FileWriter(dir + "/grouped-test.csv"));
        BufferedReader br;

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
        if (dir.contains("BlueTit")) {
            return 0;
        } else if (dir.contains("Bullfinch")) {
            return 1;
        } else if (dir.contains("CettisWarbler")) {
            return 2;
        } else if (dir.contains("Cuckoo")) {
            return 3;
        } else if (dir.contains("Goldcrest")) {
            return 4;
        } else if (dir.contains("GreatTit")) {
            return 5;
        } else if (dir.contains("noise")) {
            return 6;
        } else {
            return -1;
        }
    }

}
