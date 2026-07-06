package main.java.scraper;

import java.io.*;
import java.net.URL;

public class Scraper {

    private String key, endpoint, outputName;
    private String[] data, downloadUrls, extensions;
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

        for (String q : queries) {
            url += (q + "+");
        }

        url = url.substring(0, url.length() - 1) + "&per_page=1000&key=" + key;

        InputStream is = new URL(url).openStream();
        Reader r = new InputStreamReader(is, "UTF-8");

        data = r.readAllAsString().replaceAll(" ", "").split("\n");

        int count = 0;

        for (String line : data) {
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
                extensions[count - 1] = line.substring(line.length() - 5, line.length() - 2);
            }
            if (count > limit) {
                break;
            }
        }

        for (int i = 0; i < count - 1; i++) {
            download(downloadUrls[i], extensions[i], String.valueOf(i + 1));
        }
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
