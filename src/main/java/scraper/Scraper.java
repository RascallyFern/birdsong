package main.java.scraper;

import java.io.*;
import java.net.URL;

public class Scraper {

    private String key, endpoint, outputName, data;
    private String[] downloadUrls, extensions;
    private int files, limit;

    public Scraper() {
        key = System.getenv("XENOCANTO");
        endpoint = "https://xeno-canto.org/api/3/recordings?query=";
        files = 0;
        limit = 30;
        outputName = "";
    }

    public void scrape(String[] queries) throws IOException {
        String url = endpoint;
        String fileUrl;

        File dir = new File("./audio");
        if (!dir.exists()) {
            dir.mkdir();
        }

        for (String q : queries) {
            url += (q + "+");
        }

        url = url.substring(0, url.length() - 1) + "&per_page=1000&key=" + key;

        InputStream is = new URL(url).openStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        data = readAll(br);

        int count = 0;

        for (String line : data.replaceAll(" ", "").split("\n")) {
            if (line.contains("\"numRecordings\"")) {
                files = Integer.parseInt(line.split(":", 2)[1].replace("\"", "").replace(",", ""));
                downloadUrls = new String[files];
                extensions = new String[files];
            }
            if (line.contains("\"file\"")) {
                downloadUrls[count] = line.split(":", 2)[1].replace("\"", "").replace(",", "");
                count++;
            }
            if (line.contains("\"file-name\"")) {
                extensions[count - 1] = line.substring(line.length() - 5, line.length() - 2).toLowerCase();
            }
            if (line.contains("\"length\"")) {
                int mins = Integer.parseInt(line.split(":")[1].replace(" ", "").replaceAll("\"", "").split(":")[0]);
                if (mins < 1 || mins > 3) {
                    count--;
                }
            }
            if (count > limit) {
                break;
            }
        }

        for (int i = 0; i < count - 1; i++) {
            download(downloadUrls[i], extensions[i], String.valueOf(i + 1));
        }
    }

    private String readAll(BufferedReader br) throws IOException {
        StringBuilder sb = new StringBuilder();
        int c;
        while ((c = br.read()) != -1) {
            sb.append((char) c);
        }
        return sb.toString();
    }

    private void download(String url, String extension, String label) {
        try {
            BufferedInputStream in = new BufferedInputStream(new URL(url).openStream());

            File dir = new File("./audio/" + outputName);
            if (!dir.exists()) {
                dir.mkdir();
            }

            FileOutputStream fos = new FileOutputStream("./audio/" + outputName + "/" + outputName + "_" + label + "." + extension);

            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(buffer, 0, 1024)) != -1) {
                fos.write(buffer, 0 , bytesRead);
            }

            fos.close();
            System.out.println("File " + outputName + "_" + label + "." + extension + " downloaded successfully.");

        } catch (IOException e) {
            System.out.println("Error downloading from: " + url);
        }
    }

    public void setOutputName(String name) {
        outputName = name;
    }
}
