package main.java.cnn.layers.activation;

public class Sigmoid1D {

    private double[] input, output;
    private double[] dInput;
    private int size;

    public double[] forwardPass(double[] input) {
        this.input = input;
        size = input.length;

        output = new double[size];
        dInput = new double[size];

        for (int i = 0; i < size; i++) {
            output[i] = 1 / (1 + (Math.pow(Math.E, -input[i])));
        }

        return output;
    }

    public double[] backwardPass(double[] dInput) {
        this.dInput = dInput;

        return dInput;
    }

    public double[] getOutput() {
        return output;
    }
}
