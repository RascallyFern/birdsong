package main.java.cnn;
import main.java.cnn.layers.ConvolutionLayer;
import main.java.cnn.layers.MaxPoolLayer;
import java.util.Random;

public class Main {
    public static void main(String[] args) {
        int inputSize = 16, channels = 1;

        double[][][] input = new double[1][inputSize][inputSize];

        for (int y = 0; y < inputSize; y++) {
            for (int x = 0; x < inputSize; x++) {
                input[0][y][x] = (new Random().nextDouble() - 0.5) * 2;
            }
        }

        ConvolutionLayer c1 = new ConvolutionLayer(inputSize, channels, 2, 3, 1);
        MaxPoolLayer p1 = new MaxPoolLayer(c1.getOutputSize(), c1.getFilterCount(), 2, -1);
        ConvolutionLayer c2 = new ConvolutionLayer(p1.getOutputSize(), p1.getFilterCount(), 4, 3, 1);
        MaxPoolLayer p2 = new MaxPoolLayer(c2.getOutputSize(), c2.getFilterCount(), 2, -1);

        c1.forwardPass(input);
        p1.forwardPass(Activation.relu(c1.getOutput()));
        c2.forwardPass(Activation.relu(p1.getOutput()));
        p2.forwardPass(Activation.relu(c2.getOutput()));
    }
}