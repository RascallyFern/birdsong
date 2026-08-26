package main.java.detector;

import javax.sound.sampled.*;

public class Microphone {

    private TargetDataLine line;
    private AudioFormat format;

    public Microphone() throws LineUnavailableException {
        format = new AudioFormat(48000, 16, 1, true, false);

        DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
        line = (TargetDataLine) AudioSystem.getLine(info);
        line.open(format);
    }

    public void start() {
        line.start();
    }

    public double[] record(int sampleCount) {
        byte[] buffer = new byte[sampleCount * 2];
        int bytesRead = 0;

        while (bytesRead < buffer.length) {
            bytesRead += line.read(buffer, bytesRead, buffer.length - bytesRead);
        }

        double[] samples = new double[sampleCount];

        for (int i = 0; i < sampleCount; i++) {
            int low = buffer[i * 2] & 0xff;
            int high = buffer[i * 2 + 1];

            short val = (short) ((high << 8) | low);
            samples[i] = val / 32768.0;
        }

        return samples;
    }

    public void stop() {
        line.stop();
        line.close();
    }

    public int getSampleRate() {
        return (int) format.getSampleRate();
    }
}
