package main.java.cnn.layers;
import java.util.Arrays;
import java.util.Random;

public class ConvolutionLayer {
    //3D array, x and y for feature map, z axis for each individual feature map from different kernels
    //e.g. input[0] for 1st fm, input[2][1][1] for (1,1) in fm 2.

    private double[][][][] filters;
    private double[][][] input;
    private double[][][] output;

    private double bias = 0;
    private int inputSize;
    private int outputSize;
    private int filterSize;

    Random r = new Random();


    public ConvolutionLayer(double[][][] input, int filterNum, int filterSize) {
        this.input = input;
        this.inputSize = input[0].length;
        this.filterSize = filterSize;
        this.outputSize = inputSize - filterSize + 1;
        initFilters(filterNum);

    }

    public ConvolutionLayer(int filterNum, int filterSize) {
        this.inputSize = input[0].length;
        this.filterSize = filterSize;
        this.outputSize = inputSize - filterSize + 1;
        initFilters(filterNum);

    }

    private void initFilters(int filterNum) {
        filters = new double[filterNum][input.length][filterSize][filterSize];

        for (int filter = 0; filter < filters.length; filter++) {
            for (int z = 0; z < input.length; z++) {
                for (int y = 0; y < filterSize; y++) {
                    for (int x = 0; x < filterSize; x++) {
                        filters[filter][z][y][x] = (r.nextDouble() * 2) - 1;
                    }
                }
            }
        }
        System.out.println(Arrays.deepToString(filters));
    }

    public void forwardPass() {
        int filterNum = filters.length;
        int channelNum = input.length;

        output = new double[filterNum][outputSize][outputSize];

        for (int f = 0; f < filterNum; f++) {
            for (int y = 0; y < outputSize; y++) {
                for (int x = 0; x < outputSize; x++) {

                    double sum = 0;

                    for (int c = 0; c < channelNum; c++) {
                        for (int fY = 0; fY < filterSize; fY++) {
                            for (int fX = 0; fX < filterSize; fX++) {
                                sum += input[c][y + fY][x + fX] * filters[f][c][fY][fX];
                            }
                        }
                    }

                    output[f][y][x] = sum + bias;

                }
            }
        }

        System.out.println(Arrays.deepToString(output));
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
}
