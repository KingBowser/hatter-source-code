package me.hatter.tools.commons.io;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import me.hatter.tools.commons.environment.Environment;
import me.hatter.tools.commons.log.LogTool;
import me.hatter.tools.commons.log.LogTools;

public class SysOutUtil {

    private static final LogTool   logTool        = LogTools.getLogTool(SysOutUtil.class);

    private static volatile String OUTPUT_CHARSET = null;

    public static abstract class Output {

        public void print(char c) {
            print(String.valueOf(c));
        }

        public void println(String str) {
            print(str + Environment.LINE_SEPARATOR);
        }

        abstract public void print(String str);
    }

    public static void setOutputCharset(String charset) {
        if (OUTPUT_CHARSET != null) {
            logTool.warn("Output charset is alreadly set!");
            return;
        }
        OUTPUT_CHARSET = charset;
    }

    private static String getOutputCharset() {
        if (OUTPUT_CHARSET != null) return OUTPUT_CHARSET;
        OUTPUT_CHARSET = System.getProperty("hatter.commons.output.charset");
        return OUTPUT_CHARSET;
    }

    public static final Output stdout = new Output() {

                                          @Override
                                          public void print(String str) {
                                              if (getOutputCharset() == null) {
                                                  System.out.print(str);
                                              } else {
                                                  try {
                                                      System.out.write(toBytes(str));
                                                      if (str.indexOf('\n') >= 0) {
                                                          System.out.flush();
                                                      }
                                                  } catch (IOException e) {
                                                      throw new RuntimeException(e);
                                                  }
                                              }
                                          }
                                      };

    public static final Output errout = new Output() {

                                          @Override
                                          public void print(String str) {
                                              if (getOutputCharset() == null) {
                                                  System.err.print(str);
                                              } else {
                                                  try {
                                                      System.err.write(toBytes(str));
                                                      if (str.indexOf('\n') >= 0) {
                                                          System.err.flush();
                                                      }
                                                  } catch (IOException e) {
                                                      throw new RuntimeException(e);
                                                  }
                                              }
                                          }
                                      };

    private static byte[] toBytes(String str) throws UnsupportedEncodingException {
        return (str == null) ? new byte[0] : str.getBytes(OUTPUT_CHARSET);
    }
}
