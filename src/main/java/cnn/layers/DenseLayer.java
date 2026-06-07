package main.java.cnn.layers;

import java.util.Arrays;
import java.util.Random;

public class DenseLayer {

    private double[] input;
    private double[] output;
    private double[] bias;
    private double[][] weights;
    private int inputSize, outputSize;

    private Random r = new Random();

    public DenseLayer(int inputSize, int outputSize) {
        input = new double[inputSize];
        output = new double[outputSize];
        weights = new double[outputSize][inputSize];
        bias = new double[outputSize];

        this.inputSize = inputSize;
        this.outputSize = outputSize;

        Arrays.fill(bias, 0.0);

        // Xavier init for weights
        double limit = Math.sqrt(6.0 / (inputSize + outputSize));

        for (int i = 0; i < outputSize; i++) {
            for (int j = 0; j < inputSize; j++) {
                weights[i][j] = (r.nextDouble() * 2 * limit) - limit;
            }
        }
    }

    public void forwardPass(double[] input) {
        this.input = input;

        for (int i = 0; i < output.length; i++) {
            double sum = 0;

            for (int j = 0; j < input.length; j++) {
                sum += weights[i][j] * input[j];
            }

            output[i] = sum + bias[i];
        }
    }

    public void forwardPass(double[][][] input) {
        forwardPass(flatten(input));
    }

    private double[] flatten(double[][][] input) {
        double[] flattened = new double[input.length * input[0].length * input[0][0].length];

        for (int i = 0; i < input.length; i++) {
            for (int j = 0; j < input[i].length; j++) {
                for (int k = 0; k < input[i][j].length; k++) {
                    flattened[k + (j * input[0][0].length) + (i * input[0].length * input[0][0].length)] = input[i][j][k];
                }
            }
        }

        return flattened;
    }

    public double[] getOutput() {
        return output;
    }

    public int getOutputSize() {
        return outputSize;
    }

}
