package main.java.cnn.layers;

import java.util.Arrays;
import java.util.Random;

public class DenseLayer {

    private double[] input, output, bias;
    private double[] dInput, dOutput;
    private double[][] weights;
    private int inputSize, outputSize;
    private double learningRate;

    private Random r = new Random();

    public DenseLayer(int inputSize, int outputSize) {
        input = new double[inputSize];
        output = new double[outputSize];
        weights = new double[outputSize][inputSize];
        bias = new double[outputSize];

        this.inputSize = inputSize;
        this.outputSize = outputSize;
        learningRate = 0.001;

        Arrays.fill(bias, 0.0);

        // Xavier init for weights
        double limit = Math.sqrt(6.0 / (inputSize + outputSize));

        for (int i = 0; i < outputSize; i++) {
            for (int j = 0; j < inputSize; j++) {
                weights[i][j] = (r.nextDouble() * 2 * limit) - limit;
            }
        }
    }

    public double[] forwardPass(double[] input) {
        this.input = input;

        for (int i = 0; i < output.length; i++) {
            double sum = 0;

            for (int j = 0; j < input.length; j++) {
                sum += weights[i][j] * input[j];
            }

            output[i] = sum + bias[i];
        }

        return output;
    }

    public double[] forwardPass(double[][][] input) {
        forwardPass(flatten(input));
        return output;
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

    public double[] backwardPass(double[] dOutput) {
        dInput = new double[inputSize];

        double clip = 1;
        for (int i = 0; i < dOutput.length; i++) {
            dOutput[i] = (dOutput[i] > clip ? clip : dOutput[i]);
            dOutput[i] = (dOutput[i] < -clip ? -clip : dOutput[i]);
        }
        this.dOutput = dOutput;

        //w.r.t bias
        for (int i = 0; i < outputSize; i++) {
            bias[i] -= learningRate * dOutput[i];
        }

        //w.r.t input
        for (int i = 0; i < inputSize; i++) {
            for (int j = 0; j < outputSize; j++) {
                dInput[i] += weights[j][i] * dOutput[j];
            }
        }

        //w.r.t weights
        for (int i = 0; i < outputSize; i++) {
            for (int j = 0; j < inputSize; j++) {
                weights[i][j] -= learningRate * dOutput[i] * input[j];
            }
        }

        return dInput;
    }

    public double[] getOutput() {
        return output;
    }

    public int getOutputSize() {
        return outputSize;
    }

}
