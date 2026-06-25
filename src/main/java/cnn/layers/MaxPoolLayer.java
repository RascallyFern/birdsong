package main.java.cnn.layers;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Arrays;

public class MaxPoolLayer {
    private double[][][] input, dInput;
    private double[][][] output, dOutput;
    private int inputSize, channels, filterSize, stride, outputSize;

    public MaxPoolLayer(int inputSize, int channels, int filterSize, int stride) {
        initVars(inputSize, channels, filterSize, stride);
    }

    private void initVars(int inputSize, int channels, int filterSize, int stride) {
        this.inputSize = inputSize;
        this.channels = channels;
        this.filterSize = filterSize;
        this.stride = (stride > 0 ? stride : filterSize); // e.g. if -1 given then matches filter size
        this.outputSize = ((inputSize - filterSize) / this.stride) + 1;
        output = new double[channels][outputSize][outputSize];
    }

    public double[][][] forwardPass(double[][][] input) {
        this.input = input;

        for (int c = 0; c < channels; c++) {
            for (int y = 0; (y/stride) < outputSize; y += stride) {
                for (int x = 0; (x/stride) < outputSize; x += stride) {

                    double max = Double.NEGATIVE_INFINITY;

                    for (int fY = 0; fY < filterSize; fY++) {
                        for (int fX = 0; fX < filterSize; fX++) {
                            max = Math.max(max, input[c][y + fY][x + fX]);
                        }
                    }

                    output[c][y/stride][x/stride] = max;
                }
            }
        }

        return output;
    }

    public double[][][] backwardPass(double[][][] dOutput) {
        this.dOutput = dOutput;
        dInput = new double[channels][inputSize][inputSize];

        for (int c = 0; c < channels; c++) {
            for (int y = 0; (y/stride) < outputSize; y += stride) {
                for (int x = 0; (x/stride) < outputSize; x += stride) {

                    double max = output[c][y/stride][x/stride];

                    int lastX = -1, lastY = -1;

                    for (int fY = 0; fY < filterSize; fY++) {
                        for (int fX = 0; fX < filterSize; fX++) {
                            if (input[c][y +  fY][x + fX] == max) {
                                lastX = fX;
                                lastY = fY;
                            }
                        }
                    }

                    if (lastX != -1 && lastY != -1) {
                        dInput[c][y + lastY][x + lastX] += dOutput[c][y/stride][x/stride];
                    }

                    output[c][y/stride][x/stride] = max;
                }
            }
        }
        return dInput;
    }

    public double[][][] getOutput() {
        return output;
    }

    public int getOutputSize() {
        return outputSize;
    }

    public int getFlattenedSize() {
        return channels * outputSize * outputSize;
    }

    public int getFilterCount() { return channels; }

    public void exportToCSV(BufferedWriter bw) throws IOException {
        //int inputSize, int channels, int filterSize, int stride
        bw.write(String.format("pool,%d,%d,%d,%d\n",inputSize,channels,filterSize,stride));
    }

    public void importFromCSV(BufferedReader br) throws IOException {
        String line = br.readLine();
        String[] split = line.split(",");
        int[] vars = new int[split.length - 1];

        if (!split[0].equals("pool")) {
            System.out.println("Import does not match pooling layer!");
            return;
        }

        for (int i = 0; i < split.length - 1; i++) {
            vars[i] = Integer.parseInt(split[i+1]);
        }

        initVars(vars[0], vars[1], vars[2], vars[3]);
    }
}
