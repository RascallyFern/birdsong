package main.java.audio;

import main.java.fft.Complex;
import main.java.fft.FFT;

public class SpectrogramGenerator {

    private final int windowSize = 2048;
    private final int hopSize = 1024;
    private final int minFreq = 0;
    private final int maxFreq = 12000;

    private WavReader wav;
    private double[] samples, spectrum;
    private double[][] image;
    private int sampleRate, bandMin, bandMax;

    public double[][] generateSpectrogram(WavReader wav) {
        samples = wav.getSamples();
        sampleRate = (int) wav.getFormat().getSampleRate();
        bandMin = 0;
        bandMax = Math.min(windowSize / 2, (maxFreq * windowSize) / sampleRate);

        double maxMag = 0f;
        int frames = (samples.length - windowSize) / hopSize + 1;
        int bands = bandMax - bandMin;
        image = new double[bands][frames];

        for (int i = 0; i < frames; i++) {
            double[] window = new double[windowSize];
            System.arraycopy(samples, i * hopSize, window, 0, windowSize);
            getSpectrum(window);

            for (int b = 0; b < bands; b++) {
                image[b][i] = spectrum[b];
                if (spectrum[b] > maxMag) {
                    maxMag = spectrum[b];
                }
            }
        }

        for (int i = 0; i < image.length; i++) {
            for (int j = 0; j < image[0].length; j++) {
                image[i][j] /= maxMag;
            }
        }

        return image;
    }

    private double[] getSpectrum(double[] window) {
        int sampleCount = window.length;
        spectrum = new double[bandMax - bandMin];
        double hann;

        Complex[] complexSamples = new Complex[sampleCount];

        for (int i = 0; i < sampleCount; i++) {
            hann = 0.5 * (1 - Math.cos(2 * Math.PI * i / (sampleCount - 1)));
            complexSamples[i] = new Complex(window[i] * hann, 0);
        }

        Complex[] complexSpectrum = FFT.fft(complexSamples);

        for (int i = bandMin, idx = 0; i < bandMax; i++, idx++) {
            spectrum[idx] = Math.log(1 + complexSpectrum[i].mag());
        }

        return spectrum;
    }
}
