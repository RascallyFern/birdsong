package main.java.cnn;

import main.java.cnn.layers.*;
import main.java.cnn.layers.activation.*;

import java.util.Arrays;

public class CNN {

    private ConvolutionLayer c1, c2;
    private MaxPoolLayer p1, p2;
    private DenseLayer d1, d2;
    private ReLU3D r1, r2;
    private ReLU1D r3;
    private Sigmoid1D s1;

    private double[][][] trainImages, testImages;
    private int[] trainLabels, testLabels;
    private int inputDim, testSize, trainSize, outputs;

    public CNN(int inputDim) {
        this.inputDim = inputDim;
        c1 = new ConvolutionLayer(inputDim, 1, 32, 3, 1);
        r1 = new ReLU3D();
        p1 = new MaxPoolLayer(c1.getOutputSize(), c1.getFilterCount(), 2, -1);
        c2 = new ConvolutionLayer(p1.getOutputSize(), p1.getFilterCount(), 64, 3, 1);
        r2 = new ReLU3D();
        p2 = new MaxPoolLayer(c2.getOutputSize(), c2.getFilterCount(), 2, -1);
        d1 = new DenseLayer(p2.getFlattenedSize(), 128);
        r3 = new ReLU1D();
        d2 = new DenseLayer(d1.getOutputSize(), 10);
        s1 = new Sigmoid1D();
        outputs = 10;
    }

    private double[] forward(double[][] input) {
        double[][][] x = new double[1][inputDim][inputDim];
        x[0] = input;

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

        return y;
    }

    private void backward(double[] loss) {
        double[] x = s1.backwardPass(loss);
        x = d2.backwardPass1D(x);
        x = r3.backwardPass(x);

        double[][][] y = d1.backwardPass3D(x);
        y = p2.backwardPass(y);
    }

    public void train(int epochs) {
        double[] predictions = null;
        for (int e = 0; e < epochs; e++) {
            double avgAcc = 0;

            for (int i = 0; i < testImages.length; i++) {
                predictions = forward(testImages[i]);
                avgAcc += predictions[testLabels[i]];
            }

            System.out.println("Accuracy: " + (avgAcc / testLabels.length));

            for (int i = 0; i < trainImages.length; i++) {
                predictions = forward(trainImages[i]);
                backward(Functions.bceGradients(predictions, trainLabels[i]));
            }
            System.out.println("Label " + trainLabels[trainImages.length - 1] + ": " + Arrays.toString(predictions));
        }
    }

    public void setData(double[][][] trainImages, double[][][] testImages) {
        this.trainImages = trainImages;
        this.testImages = testImages;
        trainSize = trainImages.length;
        testSize = testImages.length;
    }

    public void setLabels(int[] trainLabels, int[] testLabels) {
        this.trainLabels = trainLabels;
        this.testLabels = testLabels;
    }
}
