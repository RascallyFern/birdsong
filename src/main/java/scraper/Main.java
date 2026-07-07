package main.java.scraper;

import java.io.IOException;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;

public class Main {
    public static void main(String[] args) {
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
            s.setOutputName("BlackHeadedGull");
            s.scrape(new String[]{"gen:chroicocephalus", "ssp:ridibundus", "smp:44100"});
            s.setOutputName("Kingfisher");
            s.scrape(new String[]{"gen:alcedo", "ssp:atthis", "smp:44100"});
            s.setOutputName("Swift");
            s.scrape(new String[]{"gen:apus", "ssp:apus", "smp:44100"});
            s.setOutputName("Yellowhammer");
            s.scrape(new String[]{"gen:emberiza", "ssp:citrinella", "smp:44100"});
            s.setOutputName("Bullfinch");
            s.scrape(new String[]{"gen:pyrrhula", "ssp:pyrrhula", "smp:44100"});
            s.setOutputName("Cuckoo");
            s.scrape(new String[]{"gen:cuculus", "ssp:canorus", "smp:44100"});
            s.setOutputName("LittleTern");
            s.scrape(new String[]{"gen:sternula", "ssp:albifrons", "smp:44100"});
            s.setOutputName("BeardedReedling");
            s.scrape(new String[]{"gen:panurus", "ssp:biarmicus", "smp:44100"});
            s.setOutputName("Goldcrest");
            s.scrape(new String[]{"gen:regulus", "ssp:regulus", "smp:44100"});
            s.setOutputName("LongTailedTit");
            s.scrape(new String[]{"gen:aegithalos", "ssp:caudatus", "smp:44100"});
            s.setOutputName("CettisWarbler");
            s.scrape(new String[]{"gen:cettia", "ssp:cetti", "smp:44100"});
            s.setOutputName("Magpie");
            s.scrape(new String[]{"gen:pica", "ssp:pica", "smp:44100"});
            s.setOutputName("Jackdaw");
            s.scrape(new String[]{"gen:coloeus", "ssp:monedula", "smp:44100"});

        } catch (IOException e) {
            System.out.println(e);
        }
    }
}
