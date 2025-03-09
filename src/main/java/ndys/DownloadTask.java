package ndys;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownloadTask implements Runnable {
    private final String url;
    private final long start;
    private final long end;
    private final String outputFile;

    public DownloadTask(String url, long start, long end, String outputFile) {
        this.url = url;
        this.start = start;
        this.end = end;
        this.outputFile = outputFile;
    }

    @Override
    public void run() {
        try {
            URL fileUrl = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) fileUrl.openConnection();
            connection.setRequestProperty("Range", "bytes=" + start + "-" + end);
            int responseCode = connection.getResponseCode();
            if (responseCode != 206) { // 206 = Partial Content
                throw new IOException("Server does not support range requests (HTTP " + responseCode + ")");
            }

            try (InputStream in = connection.getInputStream();
                 FileOutputStream out = new FileOutputStream(outputFile)) {
                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = in.read(buffer)) != -1) {
                    out.write(buffer, 0, bytesRead);
                }
            }
            connection.disconnect();
        } catch (IOException e) {
            System.err.println("DownloadTask failed for " + outputFile + ": " + e.getMessage());
        }
    }
}
