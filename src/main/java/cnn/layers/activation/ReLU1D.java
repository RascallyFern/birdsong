package main.java.cnn.layers.activation;

public class ReLU1D {

    private double[] input;
    private double[] output;

    public double[] forwardPass(double[] input) {
        this.input = input;
        output = new double[input.length];

        for (int i = 0; i < input.length; i++) {
            output[i] = (input[i] > 0 ? input[i] : 0);
        }

        return output;
    }

    public double[] getOutput() {
        return output;
    }
}
