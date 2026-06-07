package main.java.cnn;
import main.java.cnn.layers.ConvolutionLayer;
import main.java.cnn.layers.DenseLayer;
import main.java.cnn.layers.MaxPoolLayer;
import main.java.cnn.layers.activation.ReLU1D;
import main.java.cnn.layers.activation.ReLU3D;
import main.java.cnn.layers.activation.Sigmoid1D;

import java.util.Arrays;
import java.util.Random;

public class Main {
    public static void main(String[] args) {
        int inputSize = 64, channels = 1;
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
        ReLU3D r1 = new ReLU3D();
        MaxPoolLayer p1 = new MaxPoolLayer(c1.getOutputSize(), c1.getFilterCount(), 2, -1);
        ConvolutionLayer c2 = new ConvolutionLayer(p1.getOutputSize(), p1.getFilterCount(), 4, 3, 1);
        ReLU3D r2 = new ReLU3D();
        MaxPoolLayer p2 = new MaxPoolLayer(c2.getOutputSize(), c2.getFilterCount(), 2, -1);
        DenseLayer d1 = new DenseLayer(p2.getFlattenedSize(), 32);
        ReLU1D r3 = new ReLU1D();
        DenseLayer d2 = new DenseLayer(d1.getOutputSize(), 10);
        Sigmoid1D s1 = new Sigmoid1D();

        double[][][] x = input;

        x = c1.forwardPass(x);
        x = r1.forwardPass(x);
        x = p1.forwardPass(x);

        x = c2.forwardPass(x);
        x = r2.forwardPass(x);
        x = p2.forwardPass(x);

        double[] y = d1.forwardPass(x);
        y = r3.forwardPass(y);
        y = d2.forwardPass(y);
        y = s1.forwardPass(y);

        System.out.println(Arrays.toString(y));
    }
}