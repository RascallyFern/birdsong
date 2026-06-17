package main.java.cnn;
import main.java.cnn.layers.ConvolutionLayer;
import main.java.cnn.layers.DenseLayer;
import main.java.cnn.layers.MaxPoolLayer;
import main.java.cnn.layers.activation.ReLU1D;
import main.java.cnn.layers.activation.ReLU3D;
import main.java.cnn.layers.activation.Sigmoid1D;

public class Main {
    public static void main(String[] args) {
        Dataset train = new Dataset("./mnistcsv/mnist_train.csv");

        double[][][] images = train.getData();
        int[] labels = train.getLabel();
        int imageCount = train.getImageCount();

        int inputSize = 28;

        double[][][] input = new double[1][inputSize][inputSize];
        input[0] = images[0];

        ConvolutionLayer c1 = new ConvolutionLayer(inputSize, 1, 4, 3, 1);
        ReLU3D r1 = new ReLU3D();
        MaxPoolLayer p1 = new MaxPoolLayer(c1.getOutputSize(), c1.getFilterCount(), 2, -1);
        ConvolutionLayer c2 = new ConvolutionLayer(p1.getOutputSize(), p1.getFilterCount(), 16, 3, 1);
        ReLU3D r2 = new ReLU3D();
        MaxPoolLayer p2 = new MaxPoolLayer(c2.getOutputSize(), c2.getFilterCount(), 2, -1);
        DenseLayer d1 = new DenseLayer(p2.getFlattenedSize(), 128);
        ReLU1D r3 = new ReLU1D();
        DenseLayer d2 = new DenseLayer(d1.getOutputSize(), 10);
        Sigmoid1D s1 = new Sigmoid1D();

        for (int i = 0; i < train.getImageCount(); i++) {
            input[0] = images[i];
            double[][][] x = input;

            x = c1.forwardPass(x);
            x = r1.forwardPass(x);
            x = p1.forwardPass(x);

            x = c2.forwardPass(x);
            x = r2.forwardPass(x);
            x = p2.forwardPass(x);

            double[] y = d1.forwardPass(x);
            y = r3.forwardPass(y);
            y = d2.forwardPass(y);
            y = s1.forwardPass(y);
        }
    }
}