package main.java.cnn;

public class Functions {
    public static double bce(double[] p, double y) {
        // p - predictions, y - actual labels
        double sum = 0;
        double small = Math.pow(10, -15);
        double actual, clippedPred;

        for (int i = 0; i < p.length; i++) {
            actual = (y == i ? 1 : 0);
            clippedPred = Math.max(small, Math.min(1.0 - small, p[i]));

            sum -= ((actual * Math.log(clippedPred)) + ((1 - actual) * Math.log(1 - clippedPred)));
        }

        return sum / (p.length);
    }
}
