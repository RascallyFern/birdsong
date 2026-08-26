package main.java.audio;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.SourceDataLine;
import java.io.File;

public class WavReader {

    private File wavFile;
    private AudioInputStream ais;
    private AudioFormat format;
    private SourceDataLine line;
    private double[] samples;

    public WavReader(String wavDir) {
        wavFile = new File(wavDir);
        init();
        readSamples();
        System.out.println(wavDir);
    }

    private void init() {
        try {
            ais = createDecodedStream();
            format = ais.getFormat();
            line = AudioSystem.getSourceDataLine(format);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private AudioInputStream createDecodedStream() {
        try {
            AudioInputStream originalStream = AudioSystem.getAudioInputStream(wavFile);
            AudioFormat targetFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 48000, 16, 1, 2, 48000, false);

            return AudioSystem.getAudioInputStream(targetFormat, originalStream);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void readSamples() {
        try {
            ais = createDecodedStream();
            byte[] buffer = ais.readAllBytes();
            int numSamples = buffer.length / 2;
            samples = new double[numSamples];

            for (int i = 0; i < numSamples; i++) {
                int low = buffer[i * 2] & 0xFF;
                int high = buffer[i * 2 + 1];

                short sample = (short) ((high << 8) | low);

                samples[i] = sample / 32768.0;
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public double[] snipSeconds(float start, float length) {
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

    public double[] snipSamples(int start, int length) {
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

    public AudioFormat getFormat() {
        return format;
    }

    public double[] getSamples() {
        return samples;
    }
}
