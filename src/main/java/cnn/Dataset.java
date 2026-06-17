package main.java.cnn;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Dataset {

    private Scanner s, counter;
    private double[][][] data;
    private int[] label;
    private int imageCount, width, height;

    public Dataset(String dir) {
        try {
            s = new Scanner(new File(dir));
            counter = new Scanner(new File(dir));
            readFile();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private void readFile() {
        String[] split;
        int line = 0;
        String[] start = s.nextLine().split(",");

        width = Integer.parseInt(start[start.length - 1].split("x")[0]);
        height = Integer.parseInt(start[start.length - 1].split("x")[0]);

        imageCount = -1;
        while (counter.hasNext()) {
            counter.nextLine();
            imageCount++;
        }

        label = new int[imageCount];
        data = new double[imageCount][height][width];

        while (s.hasNext()) {
            split = s.nextLine().split(",");
            label[line] = Integer.parseInt(split[0]);
            for (int i = 0; i < split.length - 1; i++) {
                data[line][(int) Math.floor((i) / height)][(i % width)] = (double) Integer.parseInt(split[i + 1]) / 255;
            }
            line++;
        }
    }

    public int[] getLabel() {
        return label;
    }

    public double[][][] getData() {
        return data;
    }

    public int getImageCount() {
        return imageCount;
    }
}
