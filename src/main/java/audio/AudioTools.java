package main.java.audio;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

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

        if (dir.contains(".wav") | dir.contains(".mp3")) {
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

}
