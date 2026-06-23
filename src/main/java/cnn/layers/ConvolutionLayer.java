package main.java.cnn.layers;
import java.util.Arrays;
import java.util.Random;

public class ConvolutionLayer {
    //3D array, x and y for feature map, z axis for each individual feature map from different kernels
    //e.g. input[0] for 1st fm, input[2][1][1] for (1,1) in fm 2.

    private double[][][][] filters;
    private double[][][] input;
    private double[][][] output;

    private double[] bias;
    private int inputSize, outputSize, filterSize, filterNum, channels, stride;
    private double learningRate = 0.0001;

    private Random r = new Random();

    public ConvolutionLayer(int inputSize, int channels, int filterNum, int filterSize, int stride) {
        this.inputSize = inputSize;
        this.filterSize = filterSize;
        this.filterNum = filterNum;
        this.channels = channels;
        this.stride = stride;
        this.outputSize = ((inputSize - filterSize) / stride) + 1;
        output = new double[filterNum][outputSize][outputSize];

        bias = new double[filterNum];
        Arrays.fill(bias, 0.0);

        initLayer(filterNum);
    }

    private void initLayer(int filterNum) {
        filters = new double[filterNum][channels][filterSize][filterSize];

        // He init for weights
        double fanIn = channels * filterSize * filterSize;
        double stdv = Math.sqrt(2.0 / fanIn);

        for (int filter = 0; filter < filters.length; filter++) {
            for (int z = 0; z < channels; z++) {
                for (int y = 0; y < filterSize; y++) {
                    for (int x = 0; x < filterSize; x++) {
                        filters[filter][z][y][x] = (r.nextGaussian() * stdv);
                    }
                }
            }
        }
    }

    public double[][][] forwardPass(double[][][] input) {
        this.input = input;

        for (int f = 0; f < filterNum; f++) {
            for (int outY = 0; outY < outputSize; outY++) {
                int y = outY * stride;
                for (int outX = 0; outX < outputSize; outX++) {
                    double sum = 0;
                    int x = outX * stride;

                    for (int c = 0; c < channels; c++) {
                        for (int fY = 0; fY < filterSize; fY++) {
                            for (int fX = 0; fX < filterSize; fX++) {
                                sum += input[c][y + fY][x + fX] * filters[f][c][fY][fX];
                            }
                        }
                    }

                    output[f][outY][outX] = sum + bias[f];

                }
            }
        }

        return output;
    }


    public double[][][][] getFilters() {
        return filters;
    }

    public double[] getBias() {
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
