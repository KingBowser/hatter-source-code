package me.hatter.tests.servlet;

import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ForeverIfrmeServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setHeader("Transfer-Encoding", "chunked");
        OutputStream out = resp.getOutputStream();

        {
            byte[] b = ("@This is a for-ever frame test:<br><hr>").getBytes("UTF-8");
            writeChunked(out, b, 0, b.length);
        }

        System.out.println("@@@");
        for (int i = 0; i < 10; i++) {
            System.out.println("@@#" + i);
            // cos.write(("@" + i + "<br><hr>").getBytes("UTF-8"));
            byte[] b = ("@" + i + "<br><hr>").getBytes("UTF-8");
            writeChunked(out, b, 0, b.length);
            try {
                Thread.sleep(TimeUnit.SECONDS.toMillis(5));
            } catch (InterruptedException e) {
            }
        }
        writeChunked(out, new byte[] {}, 0, 0);
    }

    private static final byte[] crlf = { 13, 10 };

    void writeChunked(OutputStream out, byte b[], int off, int len) throws IOException {
        String lenStr = Integer.toString(len, 16);
        byte[] lenBytes = lenStr.getBytes();
        out.write(lenBytes);
        out.write(crlf);
        if (len != 0) out.write(b, off, len);
        out.write(crlf);
        out.flush();
        System.out.println("$" + lenBytes);
    }
}
