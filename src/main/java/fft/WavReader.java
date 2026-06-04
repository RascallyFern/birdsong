package main.java.fft;

import javax.sound.sampled.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class WavReader {

    private File file;
    private AudioInputStream ais;
    private AudioFormat format;
    private SourceDataLine line;

    private int FMIN;
    private int FMAX;

    private double[] samples;
    private double[][] image;
    private double[] spectrum;
    private BufferedImage spectrogram;

    private int spectrumFrames;
    private int hopSize;
    private int windowSize;

    public WavReader(String fileName) {
        file = new File(fileName);
        FMIN = 0;
        FMAX = -1;
        hopSize = 1024;
        windowSize = 2048;
        init();
    }

    private void init() {
        try {
            ais = createDecodedStream();
            format = ais.getFormat();
            line = AudioSystem.getSourceDataLine(format);

            System.out.println(format);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private AudioInputStream createDecodedStream() {
        try {
            AudioInputStream originalStream = AudioSystem.getAudioInputStream(file);
            AudioFormat originalFormat = originalStream.getFormat();

            AudioFormat decodedFormat = new AudioFormat(
                    AudioFormat.Encoding.PCM_SIGNED,
                    originalFormat.getSampleRate(),
                    16,
                    originalFormat.getChannels(),
                    originalFormat.getChannels() * 2,
                    originalFormat.getSampleRate(),
                    false
            );

            return AudioSystem.getAudioInputStream(decodedFormat, originalStream);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void readSamples() {
        try {
            ais = createDecodedStream();
            byte[] buffer = ais.readAllBytes();
            int numSamples = buffer.length / 2;
            samples = new double[numSamples];

            for (int i = 0; i < numSamples; i++) {
                int low = buffer[i * 2] & 0xFF;
                int high = buffer[i * 2 + 1];

                short sample = (short) ((high << 8) | low);

                samples[i] = sample / 32768f;
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public double[] subSample(float start, float length) {
        int startIndex = (int) (start * format.getSampleRate());
        int numSamples = (int) (length * format.getSampleRate());
        double[] subSamples = new double[numSamples];

        if (startIndex + numSamples > samples.length) {
            subSamples = new double[samples.length - startIndex];
        }

        for (int i = 0; i < subSamples.length; i++) {
            subSamples[i] = samples[startIndex + i];
        }

        return subSamples;
    }

    public double[] subSample(int start, int length) {
        double[] subSamples = new double[length];

        if (start + length > samples.length) {
            subSamples = new double[samples.length - start];
        }

        for (int i = 0; i < subSamples.length; i++) {
            subSamples[i] = samples[start + i];
        }

        return subSamples;
    }

    public void playFile() {
        try {
            byte[] buffer = new byte[4096];
            int bytesRead;

            ais = createDecodedStream();

            line.open(format);
            line.start();

            while ((bytesRead = ais.read(buffer, 0, buffer.length)) != -1) {
                line.write(buffer, 0, bytesRead);
            }

            line.drain();
            line.stop();
            line.close();
            ais.close();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public float getPlaybackTime() {
        if (line == null) {
            return 0f;
        }

        return line.getLongFramePosition() / format.getSampleRate();
    }

    public void createImage(int frameCount) {
        double maxMag = 0f;

        for (int i = 0; i < frameCount; i++) {
            double[] window = new double[windowSize];
            System.arraycopy(samples, i * hopSize, window, 0, windowSize);
            createSpectrum(frameCount, window);

            for (int f = 0; f < spectrum.length; f++) {
                image[f][i] = spectrum[f];
                if (spectrum[f] > maxMag) {
                    maxMag = spectrum[f];
                }
            }
        }

        spectrogram = new BufferedImage(frameCount, spectrum.length, BufferedImage.TYPE_INT_RGB);

        for (int i = 0; i < image.length; i++) {
            for (int j = 0; j < image[i].length; j++) {
                image[i][j] /= maxMag;
                int grey = (int) (image[i][j] * 255);
                int rgb = (grey << 16) | (grey << 8) | grey;
                spectrogram.setRGB(j, image.length - i - 1, rgb);
            }
        }
    }

    public void createSpectrum(int frameCount, double[] samples) {
        int n = samples.length;
        int sampleRate = (int) getFormat().getSampleRate();
        int kMax = Math.min(FMAX * n / sampleRate, (n / 2));
        int kMin = Math.max((FMIN * n / sampleRate), 0);
        double hann = 0.5 * (1 - Math.cos(2 * Math.PI / (samples.length - 1)));
        kMax = (kMax <= 0 ? n / 2 : kMax);

        image = new double[kMax - kMin][frameCount];
        Complex[] complexSamples = new Complex[samples.length];

        for (int i = 0; i < samples.length; i++) {
            complexSamples[i] = new Complex(samples[i] * hann, 0);
        }

        Complex[] complexSpectrum = FFT.fft(complexSamples);
        spectrum = new double[kMax - kMin];

        for (int i = kMin, idx = 0; i < kMax; i++, idx++) {
            spectrum[idx] = Math.log(1 + complexSpectrum[i].mag());
        }

        spectrumFrames = frameCount;
    }

    public AudioFormat getFormat() {
        return format;
    }
}
