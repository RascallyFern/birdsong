package main.java.cnn.layers;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;

public class ConvolutionLayer {
    //3D array, x and y for feature map, z axis for each individual feature map from different kernels
    //e.g. input[0] for 1st fm, input[2][1][1] for (1,1) in fm 2.

    private double[][][][] filters;
    private double[][][] input, dInput;
    private double[][][] output, dOutput;

    private double[] bias;
    private int inputSize, outputSize, filterSize, filterNum, channels, stride;
    private double learningRate = 0.0001;

    private Random r = new Random();

    public ConvolutionLayer(int inputSize, int channels, int filterNum, int filterSize, int stride) {
        initVars(inputSize, channels, filterNum, filterSize, stride);
        initLayer(filterNum);
    }

    private void initVars(int inputSize, int channels, int filterNum, int filterSize, int stride) {
        this.inputSize = inputSize;
        this.filterSize = filterSize;
        this.filterNum = filterNum;
        this.channels = channels;
        this.stride = stride;
        this.dInput = new double[channels][inputSize][inputSize];
        this.outputSize = ((inputSize - filterSize) / stride) + 1;
        output = new double[filterNum][outputSize][outputSize];

        bias = new double[filterNum];
        Arrays.fill(bias, 0.0);
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

    public double[][][] backwardPass(double[][][] dOutput) {
        this.dOutput = dOutput;
        dInput = new double[channels][inputSize][inputSize];
        double sum;
        double[][][] dOutPadded = new double[dOutput.length][dOutput[0].length + 2 * (filterSize - 1)][dOutput[0][0].length + 2 * (filterSize - 1)];

        for (int c = 0; c < dOutput.length; c++) {
            for (int y = 0; y < dOutPadded[0].length; y++) {
                for (int x = 0; x < dOutPadded[0][0].length; x++) {
                    if (x < (filterSize - 1) || y < (filterSize - 1) || x >= (dOutPadded[0][0].length - (filterSize - 1)) || y >= (dOutPadded[0].length - (filterSize - 1))) {
                        dOutPadded[c][y][x] = 0;
                    } else {
                        dOutPadded[c][y][x] = dOutput[c][y-(filterSize - 1)][x-(filterSize - 1)];
                    }
                }
            }
        }

        //loss wrt input
        for (int out = 0; out < channels; out++) {
            for (int f = 0; f < filterNum; f++) {
                for (int y = 0; y < dInput[0].length; y++) {
                    for (int x = 0; x < dInput[0][0].length; x++) {
                        sum = 0;
                        for (int wY = 0; wY < filterSize; wY++) {
                            for (int wX = 0; wX < filterSize; wX++) {
                                //flip filters for backprop
                                sum += dOutPadded[f][y + wY][x + wX] * filters[f][out][filterSize - 1 - wY][filterSize - 1 - wX];
                            }
                        }
                        dInput[out][y][x] += sum;
                    }
                }
            }
        }

        //loss wrt weights
        for (int f = 0; f < dOutput.length; f++) {
            for (int y = 0; y < filterSize; y++) {
                for (int x = 0; x < filterSize; x++) {
                    for (int c = 0; c < channels; c++) {
                        sum = 0;
                        for (int doutY = 0; doutY < dOutput[0].length; doutY++) {
                            for (int doutX = 0; doutX < dOutput[0][0].length; doutX++) {
                                sum += input[c][y + doutY][x + doutX] * dOutput[f][doutY][doutX];
                            }
                        }
                        filters[f][c][y][x] -= sum * learningRate;
                    }
                }
            }
        }

        //lost wrt bias
        for (int b = 0; b < dOutput.length; b++) {
            sum = 0;
            for (int y = 0; y < dOutput[0].length; y++) {
                for (int x = 0; x < dOutput[0][0].length; x++) {
                    sum += dOutput[b][y][x];
                }
            }
            bias[b] -= learningRate * sum;
        }

        return dInput;
    }

    public void exportToCSV(BufferedWriter bw) throws IOException {
        //int inputSize, int channels, int filterNum, int filterSize, int stride
        bw.write(String.format("conv,%d,%d,%d,%d,%d\n",inputSize,channels,filterNum,filterSize,stride));
        for (int f = 0; f < filterNum; f++) {
            for (int c = 0; c < channels; c++) {
                for (int row = 0; row < filterSize; row++) {
                    bw.write(Arrays.toString(filters[f][c][row]).replaceAll("\\[","").replaceAll("\\]", "").replace(" ", "") + "\n");
                }
            }
        }
        bw.write(Arrays.toString(bias).replaceAll("\\[","").replaceAll("\\]", "").replace(" ", "") + "\n");
    }

    public void importFromCSV(BufferedReader br) throws IOException {
        String line = br.readLine();
        String[] split = line.split(",");
        String[] filterRow;
        int[] vars = new int[split.length - 1];

        if (!split[0].equals("conv")) {
            System.out.println("Import does not match convolution layer!");
            return;
        }

        for (int i = 0; i < split.length - 1; i++) {
            vars[i] = Integer.parseInt(split[i+1]);
        }

        //int inputSize, int channels, int filterNum, int filterSize, int stride
        initVars(vars[0], vars[1], vars[2], vars[3], vars[4]);

        filters = new double[filterNum][channels][filterSize][filterSize];

        for (int f = 0; f < filterNum; f++) {
            for (int c = 0; c < channels; c++) {
                for (int row = 0; row < filterSize; row++) {
                    filterRow = br.readLine().split(",");
                    for (int w = 0; w < filterSize; w++) {
                        filters[f][c][row][w] = Double.parseDouble(filterRow[w]);
                    }
                }
            }
        }

        String[] biasImport = br.readLine().split(",");
        for (int i = 0; i < bias.length; i++) {
            bias[i] = Double.parseDouble(biasImport[i]);
        }
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
