package me.hatter.tools.commons.process;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

public class ProcessTool {

    private long   waitMills    = 500;
    private String shellCharset = "UTF-8";

    public ProcessTool() {
    }

    public ProcessTool(String shellCharset) {
        this.shellCharset = shellCharset;
    }

    public ProcessTool(String shellCharset, long waitMills) {
        this.shellCharset = shellCharset;
        this.waitMills = waitMills;
    }

    public String callProcessAsString(ProcessBuilder processBuilder) {
        try {
            return new String(callProcess(processBuilder), shellCharset);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public byte[] callProcess(ProcessBuilder processBuilder) {
        try {
            Process p = processBuilder.start();
            final InputStream is = p.getInputStream();
            final ByteArrayOutputStream baos = new ByteArrayOutputStream();

            Thread it = new Thread() {

                public void run() {
                    int b;
                    try {
                        while ((b = is.read()) != -1) {
                            baos.write(b);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            };
            it.start();
            p.waitFor();

            Thread.sleep(waitMills);
            if (it.isAlive()) {
                it.interrupt();
            }

            return baos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
