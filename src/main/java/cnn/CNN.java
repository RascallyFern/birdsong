package main.java.cnn;

import main.java.cnn.layers.ConvolutionLayer;
import main.java.cnn.layers.DenseLayer;
import main.java.cnn.layers.MaxPoolLayer;
import main.java.cnn.layers.activation.ReLU1D;
import main.java.cnn.layers.activation.ReLU3D;
import main.java.cnn.layers.activation.Sigmoid1D;

import java.io.*;
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
    private int inputDim;
    private String exportName;
    private File importFile, exportFile;

    public CNN(String importCsvName) throws FileNotFoundException {
        BufferedReader br = new BufferedReader(new FileReader(importCsvName + ".csv"));

    }

    public CNN(int inputDim) {
        this.inputDim = inputDim;
        exportName = "export";
        c1 = new ConvolutionLayer(inputDim, 1, 1, 3, 1);
        r1 = new ReLU3D();
        p1 = new MaxPoolLayer(c1.getOutputSize(), c1.getFilterCount(), 2, -1);
        c2 = new ConvolutionLayer(p1.getOutputSize(), p1.getFilterCount(), 1, 3, 1);
        r2 = new ReLU3D();
        p2 = new MaxPoolLayer(c2.getOutputSize(), c2.getFilterCount(), 2, -1);
        d1 = new DenseLayer(p2.getFlattenedSize(), 128);
        r3 = new ReLU1D();
        d2 = new DenseLayer(d1.getOutputSize(), 10);
        s1 = new Sigmoid1D();
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

    public void test() {
        int correct = 0;
        double[] predictions;

        for (int i = 0; i < testImages.length; i++) {
            predictions = forward(testImages[i]);
            if (mostConfidence(predictions) == testLabels[i]) {
                correct++;
            }
        }

        double acc = (double) correct / testImages.length;

        System.out.println("Accuracy: " + acc);
    }

    public void train(int epochs) {
        double[] predictions = null;
        for (int e = 0; e < epochs; e++) {

            test();

            for (int i = 0; i < trainImages.length; i++) {
                predictions = forward(trainImages[i]);
                backward(Functions.bceGradients(predictions, trainLabels[i]));
            }
            System.out.println("Label " + trainLabels[trainImages.length - 1] + ": " + Arrays.toString(predictions));
        }
    }

    public int mostConfidence(double[] predictions) {
        double max = Double.NEGATIVE_INFINITY;
        int index = 0;

        for (int i = 0; i < predictions.length; i++) {
            if (predictions[i] > max) {
                max = predictions[i];
                index = i;
            }
        }

        return index;
    }

    public void setData(double[][][] trainImages, double[][][] testImages) {
        this.trainImages = trainImages;
        this.testImages = testImages;
    }

    public void setLabels(int[] trainLabels, int[] testLabels) {
        this.trainLabels = trainLabels;
        this.testLabels = testLabels;
    }

    public void exportToCSV() throws IOException {
        exportFile = new File(exportName + ".csv");

        if (!exportFile.createNewFile()) {
            System.out.println("File with this name already exists!");
            return;
        }

        BufferedWriter bw = new BufferedWriter(new FileWriter(exportFile));
        c1.exportToCSV(bw);
        p1.exportToCSV(bw);
        c2.exportToCSV(bw);
        p2.exportToCSV(bw);
        d1.exportToCSV(bw);
        d2.exportToCSV(bw);
        bw.close();
    }

    public void importFromCSV(String importDir) throws IOException {
        importFile = new File(importDir);

        BufferedReader br = new BufferedReader(new FileReader(importFile));
        c1.importFromCSV(br);
        p1.importFromCSV(br);
        c2.importFromCSV(br);
        p2.importFromCSV(br);
        d1.importFromCSV(br);
        d2.importFromCSV(br);
    }
}
