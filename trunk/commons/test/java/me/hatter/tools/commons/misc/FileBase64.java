package me.hatter.tools.commons.misc;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import me.hatter.tools.commons.environment.Environment;
import me.hatter.tools.commons.io.IOUtil;

public class FileBase64 {

    public static void main(String[] args) throws IOException {
        File f = new File(Environment.USER_HOME, "gl.png");
        byte[] bs = IOUtil.readToBytesAndClose(new FileInputStream(f));

        System.out.println();
        System.out.println(Base64.byteArrayToBase64(bs));
    }
}
