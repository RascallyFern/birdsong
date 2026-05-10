package main.java.cnn;
import main.java.cnn.layers.ConvolutionLayer;

public class Main {
    public static void main(String[] args) {
        double[][][] input = new double[1][8][8];
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                input[0][y][x] = 1;
            }
        }

        ConvolutionLayer c1 = new ConvolutionLayer(input, 2, 3);

        c1.forwardPass();
    }
}