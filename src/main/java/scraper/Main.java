package main.java.scraper;

import java.io.IOException;

public class Main {
    static void main() {
        Scraper s = new Scraper();

        try {
            s.setOutputName("Mallard");
            s.scrape(new String[]{"gen:anas", "ssp:platyrhynchos", "smp:44100"});
            s.setOutputName("BlueTit");
            s.scrape(new String[]{"gen:cyanistes", "ssp:caeruleus", "smp:44100"});
            s.setOutputName("Robin");
            s.scrape(new String[]{"gen:erithacus", "ssp:rubecula", "smp:44100"});
            s.setOutputName("GreatTit");
            s.scrape(new String[]{"gen:parus", "ssp:major", "smp:44100"});
            s.setOutputName("GreatCrestedGrebe");
            s.scrape(new String[]{"gen:podiceps", "ssp:cristatus", "smp:44100"});
            s.setOutputName("WillowTit");
            s.scrape(new String[]{"gen:poecile", "ssp:kleinschmidti", "smp:44100"});

        } catch (IOException e) {

        }
    }
}
