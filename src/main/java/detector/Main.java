package main.java.detector;

public class Main {
    public static void main(String[] args) {
        try {
            LiveDetector detector = new LiveDetector();
            detector.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
