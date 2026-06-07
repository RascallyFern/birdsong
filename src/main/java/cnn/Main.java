package main.java.cnn;
import main.java.cnn.layers.ConvolutionLayer;
import main.java.cnn.layers.DenseLayer;
import main.java.cnn.layers.MaxPoolLayer;

import java.util.Arrays;
import java.util.Random;

public class Main {
    public static void main(String[] args) {
        int inputSize = 512, channels = 128;
        Random r = new Random();

        double[][][] input = new double[channels][inputSize][inputSize];

        for (int c = 0; c < channels; c++) {
            for (int y = 0; y < inputSize; y++) {
                for (int x = 0; x < inputSize; x++) {
                    input[c][y][x] = (r.nextDouble() - 0.5) * 2;
                }
            }
        }

        ConvolutionLayer c1 = new ConvolutionLayer(inputSize, channels, 2, 3, 1);
        MaxPoolLayer p1 = new MaxPoolLayer(c1.getOutputSize(), c1.getFilterCount(), 2, -1);
        ConvolutionLayer c2 = new ConvolutionLayer(p1.getOutputSize(), p1.getFilterCount(), 4, 3, 1);
        MaxPoolLayer p2 = new MaxPoolLayer(c2.getOutputSize(), c2.getFilterCount(), 2, -1);
        DenseLayer d1 = new DenseLayer(p2.getFlattenedSize(), 10);

        c1.forwardPass(input);
        p1.forwardPass(Activation.relu(c1.getOutput()));
        c2.forwardPass(Activation.relu(p1.getOutput()));
        p2.forwardPass(Activation.relu(c2.getOutput()));
        d1.forwardPass(Activation.relu(p2.getOutput()));

        System.out.println(Arrays.toString(Activation.sigmoid(d1.getOutput())));
    }
}