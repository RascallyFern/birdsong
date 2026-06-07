package main.java.cnn.layers.activation;

public class Sigmoid1D {

    private double[] input;
    private double[] output;

    public double[] forwardPass(double[] input) {
        this.input = input;
        output = new double[input.length];

        for (int i = 0; i < input.length; i++) {
            output[i] = 1 / (1 + (Math.pow(Math.E, -input[i])));
        }

        return output;
    }

    public double[] getOutput() {
        return output;
    }
}
