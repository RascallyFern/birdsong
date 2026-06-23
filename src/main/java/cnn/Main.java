package main.java.cnn;

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