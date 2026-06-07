package main.java.cnn.layers;

public class MaxPoolLayer {
    private double[][][] input;
    private double[][][] output;
    private int inputSize, channels, filterSize, stride, outputSize;

    public MaxPoolLayer(int inputSize, int channels, int filterSize, int stride) {
        this.inputSize = inputSize;
        this.channels = channels;
        this.filterSize = filterSize;
        this.stride = (stride > 0 ? stride : filterSize); // e.g. if -1 given then matches filter size
        this.outputSize = ((inputSize - filterSize) / this.stride) + 1;
    }

    public void forwardPass(double[][][] input) {
        this.input = input;
        output = new double[channels][outputSize][outputSize];

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
}
