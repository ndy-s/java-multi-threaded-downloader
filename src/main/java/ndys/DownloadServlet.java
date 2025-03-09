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
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.sendRedirect("/index.html");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String fileUrl = req.getParameter("fileUrl");
        if (fileUrl == null || fileUrl.trim().isEmpty()) {
            sendErrorResponse(resp, "Please provide a valid file URL.");
            return;
        }

        String outputDir = new File("src/main/downloads").getAbsolutePath();
        File dir = new File(outputDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();

        try {
            FileDownloader downloader = new FileDownloader(fileUrl, 4, outputDir);
            String downloadedFile = downloader.download();

            out.println("<html><body>");
            out.println("<h2>Download Complete!</h2>");
            out.println("<p>File saved at: " + downloadedFile + "</p>");
            out.println("<a href=\"/index.html\">Back</a>");
            out.println("</body></html>");
        } catch (IOException e) {
            sendErrorResponse(resp, "Download failed: " + e.getMessage());
        }
    }

    private void sendErrorResponse(HttpServletResponse resp, String message) throws IOException {
        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();
        out.println("<html><body>");
        out.println("<h2>Error</h2>");
        out.println("<p>" + message + "</p>");
        out.println("<a href=\"/index.html\">Back</a>");
        out.println("</body></html>");
    }
}
