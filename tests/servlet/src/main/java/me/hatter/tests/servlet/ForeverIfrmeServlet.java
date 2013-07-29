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
        ChunkedOutputStream cos = new ChunkedOutputStream(out);

        System.out.println("@@@");
        for (int i = 0; i < 10; i++) {
            System.out.println("@@#" + i);
            // cos.write(("@" + i + "<br><hr>").getBytes("UTF-8"));
            byte[] b = ("@" + i + "<br><hr>").getBytes("UTF-8");
            cos.writeBuf(b, 0, b.length);
            try {
                Thread.sleep(TimeUnit.SECONDS.toMillis(10));
            } catch (InterruptedException e) {
            }
        }
        cos.done();
    }
}
