package main.java.fft;

public class Main {
    public static void main(String[] args) {
        WavReader wr = new WavReader("./bluetit.wav");
        wr.readSamples();

        double[] samples = wr.subSample(0, (int) Math.pow(2, 10));
        System.out.println(samples.length);
        Complex[] complexSamples = new Complex[samples.length];

        for (int i = 0; i < samples.length; i++) {
            complexSamples[i] = new Complex(samples[i], 0);
        }

        Complex[] complexSpectrum = FFT.fft(complexSamples);
        double[] spectrum = new double[complexSpectrum.length];

        for (int i = 0; i < complexSpectrum.length; i++) {
            spectrum[i] = complexSpectrum[i].mag();
        }

        for (int i = 0; i < spectrum.length; i++) {
            double frequency = wr.getFormat().getSampleRate() / samples.length;
            System.out.println(frequency * (i + 1));
        }

    }
}
