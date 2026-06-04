package main.java.cnn.layers;
import java.util.Random;

public class ConvolutionLayer {
    //3D array, x and y for feature map, z axis for each individual feature map from different kernels
    //e.g. input[0] for 1st fm, input[2][1][1] for (1,1) in fm 2.

    private double[][][][] filters;
    private double[][][] input;
    private double[][][] output;

    private double bias = 0;
    private int inputSize, outputSize, filterSize, filterNum, channels, stride;

    Random r = new Random();

    public ConvolutionLayer(int inputSize, int channels, int filterNum, int filterSize, int stride) {
        this.inputSize = inputSize;
        this.filterSize = filterSize;
        this.filterNum = filterNum;
        this.channels = channels;
        this.stride = stride;
        this.outputSize = ((inputSize - filterSize) / stride) + 1;
        initFilters(filterNum);
    }

    private void initFilters(int filterNum) {
        filters = new double[filterNum][channels][filterSize][filterSize];

        for (int filter = 0; filter < filters.length; filter++) {
            for (int z = 0; z < channels; z++) {
                for (int y = 0; y < filterSize; y++) {
                    for (int x = 0; x < filterSize; x++) {
                        filters[filter][z][y][x] = (r.nextDouble() * 2) - 1;
                    }
                }
            }
        }
    }

    public void forwardPass(double[][][] input) {
        this.input = input;

        output = new double[filterNum][outputSize][outputSize];

        for (int f = 0; f < filterNum; f++) {
            for (int y = 0; (y/stride) < outputSize; y += stride) {
                for (int x = 0; (x/stride) < outputSize; x += stride) {

                    double sum = 0;

                    for (int c = 0; c < channels; c++) {
                        for (int fY = 0; fY < filterSize; fY++) {
                            for (int fX = 0; fX < filterSize; fX++) {
                                sum += input[c][y + fY][x + fX] * filters[f][c][fY][fX];
                            }
                        }
                    }

                    output[f][y/stride][x/stride] = sum + bias;

                }
            }
        }
    }


    public double[][][][] getFilters() {
        return filters;
    }

    public double getBias() {
        return bias;
    }

    public double[][][] getOutput() {
        return output;
    }

    public int getOutputSize() {
        return outputSize;
    }

    public int getFilterCount() { return filters.length; }
}
