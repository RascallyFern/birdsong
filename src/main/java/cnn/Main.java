package main.java.cnn;
import main.java.cnn.layers.ConvolutionLayer;
import main.java.cnn.layers.DenseLayer;
import main.java.cnn.layers.MaxPoolLayer;
import main.java.cnn.layers.activation.ReLU1D;
import main.java.cnn.layers.activation.ReLU3D;
import main.java.cnn.layers.activation.Sigmoid1D;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        Dataset train = new Dataset("./mnistcsv/mnist_train.csv");
        Dataset test = new Dataset("./mnistcsv/mnist_test.csv");

        double[][][] trainImages = train.getData();
        double[][][] testImages = test.getData();

        int[] trainLabels = train.getLabel();
        int[] testLabels = test.getLabel();

        CNN network = new CNN(28);
        network.setData(trainImages, testImages);
        network.setLabels(trainLabels, testLabels);

        network.train(10);
    }
}