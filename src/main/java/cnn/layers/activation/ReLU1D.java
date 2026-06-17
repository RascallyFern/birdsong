package main.java.cnn.layers.activation;

public class ReLU1D {

    private double[] input, dInput;
    private double[] output, dOutput;
    private int size;

    public double[] forwardPass(double[] input) {
        this.input = input;
        size = input.length;

        output = new double[size];
        dInput = new double[size];

        for (int i = 0; i < size; i++) {
            output[i] = (input[i] > 0 ? input[i] : 0);
        }

        return output;
    }

    public double[] backwardPass(double[] dOutput) {
        this.dOutput = dOutput;

        for (int i = 0; i < size; i++) {
            dInput[i] = (output[i] > 0 ? dOutput[i] : 0);
        }

        return dInput;
    }

    public double[] getOutput() {
        return output;
    }
}
