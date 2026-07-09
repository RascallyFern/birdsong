package main.java.audio;

import java.io.*;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Dictionary;

public class AudioTools {

    private ArrayList<String> paths;

    public void convertAllToWav(String path) {
        if (path.toLowerCase().contains(".mp3")) {
            File temp = new File(path.substring(0, path.length() - 3) + "wav");
            System.out.println(temp.getName());
            temp.delete();
            convertToWav(path);
            File original = new File(path);
            if (original.delete()) {
                System.out.println("Deleted: " + path);
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
        BufferedWriter bw = new BufferedWriter(new FileWriter(dir + "/grouped.csv"));
        BufferedReader br;
        ArrayList<String> files = new ArrayList<>();
        getAllPaths(dir, files);

        int count = 0;
        for (String file : files) {
            if (fileLimit >= 0 && count > fileLimit) {
                bw.close();
                return;
            }
            br = new BufferedReader(new FileReader(file));
            String readLine = br.readLine();
            String outLine = String.valueOf(getLabel(file)) + ",";
            while (readLine != null) {
                outLine += readLine.replace("\n", "");
                readLine = br.readLine();
            }
            bw.write(outLine + "\n");
            count++;
        }
        bw.close();
    }

    public int getLabel(String dir) {
        if (dir.contains("BlueTit")) {
            return 1;
        } else if (dir.contains("Bullfinch")) {
            return 2;
        } else if (dir.contains("CettisWarbler")) {
            return 3;
        } else if (dir.contains("Cuckoo")) {
            return 4;
        } else if (dir.contains("Goldcrest")) {
            return 5;
        } else if (dir.contains("GreatTit")) {
            return 6;
        } else if (dir.contains("Jackdaw")) {
            return 7;
        } else if (dir.contains("LittleTern")) {
            return 8;
        } else if (dir.contains("LongTailedTit")) {
            return 9;
        } else if (dir.contains("Magpie")) {
            return 10;
        } else {
            return -1;
        }
    }

    public double[][][] separateCalls(double[][] spectrogram) {
        return null;
    }

}
