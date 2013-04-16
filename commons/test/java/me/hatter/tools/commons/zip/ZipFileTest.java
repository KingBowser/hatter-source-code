package me.hatter.tools.commons.zip;

import java.io.IOException;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ZipFileTest {

    public static void main(String[] args) throws IOException {
        String f = "/Users/hatterjiang/Code/hatter-source-code/commons/commons-1.0.jar";
        ZipFile z = new ZipFile(f);
        System.out.println(z.getName());
        Enumeration<? extends ZipEntry> es = z.entries();
        while(es.hasMoreElements()) {
            ZipEntry e = es.nextElement();
            System.out.println(e.getName());
        }
    }
}
