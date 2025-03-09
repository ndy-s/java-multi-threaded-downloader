package ndys;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FileDownloader {
    private final String fileUrl;
    private final int threadCount;
    private final String outputDir;

    public FileDownloader(String fileUrl, int threadCount, String outputDir) {
        this.fileUrl = fileUrl;
        this.threadCount = threadCount;
        this.outputDir = outputDir;
    }

    public String download() throws IOException {
        URL url = new URL(fileUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        long fileSize = connection.getContentLengthLong();
        String fileName = fileUrl.substring(fileUrl.lastIndexOf('/') + 1);
        if (fileName.isEmpty()) {
            fileName = "downloaded_file"; // Fallback name
        }
        String outputFile = outputDir + File.separator + fileName;
        connection.disconnect();

        if (fileSize == -1) {
            throw new IOException("File size unknown or server doesn't support range requests.");
        }

        long chunkSize = fileSize / threadCount;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        File[] chunkFiles = new File[threadCount];

        // Start download threads
        for (int i = 0; i < threadCount; i++) {
            long start = i * chunkSize;
            long end = (i == threadCount - 1) ? fileSize - 1 : start + chunkSize - 1;
            String chunkPath = outputDir + File.separator + "chunk" + i + ".tmp";
            chunkFiles[i] = new File(chunkPath);
            executor.submit(new DownloadTask(fileUrl, start, end, chunkPath));
        }

        executor.shutdown();
        while (!executor.isTerminated()) {
            try {
                Thread.sleep(100); // Small delay to avoid busy-waiting
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        // Verify all chunks exist before merging
        for (File chunk : chunkFiles) {
            if (!chunk.exists()) {
                throw new IOException("Chunk file missing: " + chunk.getAbsolutePath());
            }
        }

        mergeFiles(outputFile, chunkFiles);
        return outputFile;
    }

    private void mergeFiles(String outputFile, File[] chunkFiles) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(outputFile)) {
            for (File chunk : chunkFiles) {
                try (FileInputStream fis = new FileInputStream(chunk)) {
                    fis.transferTo(fos);
                }
                chunk.delete(); // Clean up after merging
            }
        }
    }
}