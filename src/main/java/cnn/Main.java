package main.java.cnn;

import main.java.cnn.networks.CNNConv3;

public class Main {
    public static void main(String[] args) throws Exception {
        CNNConv3 network = new CNNConv3(128, 128);
        Dataset train = new Dataset("./spectrograms/csvs-20/grouped-train.csv");
        Dataset test = new Dataset("./spectrograms/csvs-20/grouped-test.csv");

        network.setLabels(train.getLabel(), test.getLabel());
        network.setData(train.getData(), test.getData());

        network.train(12);
        network.exportToCSV();
    }
}