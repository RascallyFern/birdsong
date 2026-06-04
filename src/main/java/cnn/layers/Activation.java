package main.java.cnn.layers;

public class Activation {
    public static double[][][] relu(double[][][] input) {
        double[][][] output = new double[input.length][input[0].length][input[0][0].length];
        for (int i = 0; i < input.length; i++) {
            for (int j = 0; j < input[i].length; j++) {
                for (int k = 0; k < input[i][j].length; k++) {
                    output[i][j][k] = Math.max(0, input[i][j][k]);
                }
            }
        }

        return output;
    }

    public static double[][][] sigmoid(double[][][] input) {
        double[][][] output = new double[input.length][input[0].length][input[0][0].length];
        for (int i = 0; i < input.length; i++) {
            for (int j = 0; j < input[i].length; j++) {
                for (int k = 0; k < input[i][j].length; k++) {
                    output[i][j][k] = 1 / (1 + (Math.pow(Math.E, -input[i][j][k])));
                }
            }
        }

        return output;
    }
}
