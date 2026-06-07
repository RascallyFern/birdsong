package main.java.cnn.layers.activation;

public class ReLU3D {
    private double[][][] input;
    private double[][][] output;

    public double[][][] forwardPass(double[][][] input) {
        this.input = input;
        output = new double[input.length][input[0].length][input[0][0].length];

        for (int i = 0; i < input.length; i++) {
            for (int j = 0; j < input[i].length; j++) {
                for (int k = 0; k < input[i][j].length; k++) {
                    output[i][j][k] = Math.max(0, input[i][j][k]);
                }
            }
        }

        return output;
    }

    public double[][][] getOutput() {
        return output;
    }
}
