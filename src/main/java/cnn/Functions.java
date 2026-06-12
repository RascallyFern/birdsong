package main.java.cnn;

public class Functions {
    public double bce(double[] p, double[] y) {
        // p - predictions, y - actual labels
        double sum = 0;

        for (int i = 0; i < p.length; i++) {
            sum -= ((y[i] * Math.log(p[i])) + ((1 - y[i]) * Math.log(1 - p[i])));
        }

        return sum / (p.length);
    }
}
