package main.java.cnn;

public class Functions {
    public static double[] bceGradients(double[] p, double y) {
        // p - predictions, y - actual labels

        double[] grad = new double[p.length];

        for (int i = 0; i < p.length; i++) {
            double actual = (y == i ? 1 : 0);
            grad[i] = p[i] - actual;
        }

        return grad;
    }
}
