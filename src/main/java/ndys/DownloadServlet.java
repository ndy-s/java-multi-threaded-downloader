package ndys;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(urlPatterns = "/download")
public class DownloadServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();

        String fileUrl = req.getParameter("fileUrl");
        if (fileUrl == null || fileUrl.trim().isEmpty()) {
            out.print("{\"status\": \"error\", \"message\": \"Please provide a valid file URL.\"}");
            return;
        }

        String outputDir = new File("src/main/downloads").getAbsolutePath();
        new File(outputDir).mkdirs();

        try {
            FileDownloader downloader = new FileDownloader(fileUrl, 4, outputDir);
            String downloadedFile = downloader.download();
            out.print("{\"status\": \"success\", \"file\": \"" + downloadedFile + "\"}");
        } catch (IOException e) {
            out.print("{\"status\": \"error\", \"message\": \"Download failed: " + e.getMessage() + "\"}");
        }
    }
}
