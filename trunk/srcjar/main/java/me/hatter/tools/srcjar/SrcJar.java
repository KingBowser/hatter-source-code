package me.hatter.tools.srcjar;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;

import me.hatter.tools.commons.args.UnixArgsutil;
import me.hatter.tools.commons.io.IOUtil;

public class SrcJar {

    public static void main(String[] args) {
        UnixArgsutil.parseGlobalArgs(args);

        String prefix = UnixArgsutil.ARGS.kvalue("prefix");
        String sour = UnixArgsutil.ARGS.kvalue("source");
        String dest = UnixArgsutil.ARGS.kvalue("dest");
        String charset = UnixArgsutil.ARGS.kvalue("charset", "UTF-8");

        if ((prefix == null) || (sour == null) || (dest == null)) {
            System.out.println("Arguments -rule -source -dest must be all assigned.");
            System.out.println("Usage:");
            System.out.println("  java -jar srcjar.jar -prefix <prefix> -source <source jar> -dest <dest file>");
            System.out.println("  java -cp srcjar.jar srcjar -prefix <prefix> -source <source jar> -dest <dest file>");
            System.out.println("  srcjar -prefix <prefix> -source <source jar> -dest <dest file>");
            System.out.println("    -prefix       prefix");
            System.out.println("    -source       source file");
            System.out.println("    -dest         dest file");
            System.out.println("    -charset      charset[default UTF-8]");
            System.out.println("    --verbose     verbose output");
            System.out.println("Sample:");
            System.out.println("  srcjar -prefix com.test -source a.jar -dest b.jar");

            System.exit(-1);
        }

        File fsour = new File(sour);
        File fdest = new File(dest);
        if (!fsour.exists()) {
            System.out.println("Source file not exists: " + fsour.getAbsolutePath());
            System.exit(-1);
        }
        if (fdest.exists()) {
            System.out.println("Dest file exists: " + fdest.getAbsolutePath());
            System.exit(-1);
        }
        try {
            JarFile jarsour = new JarFile(fsour);
            try {
                FileOutputStream stream = new FileOutputStream(fdest);
                JarOutputStream out = new JarOutputStream(stream, new Manifest());
                Enumeration<JarEntry> jarEntries = jarsour.entries();
                for (JarEntry je; jarEntries.hasMoreElements();) {
                    je = jarEntries.nextElement();
                    if (je.getName().endsWith("/")) {
                        continue;
                    }

                    if (UnixArgsutil.ARGS.flags().contains("verbose")) {
                        System.out.println("Processing: " + je.getName());
                    }

                    String jsource = IOUtil.readToString(jarsour.getInputStream(je), charset);

                    JarEntry jarAdd = new JarEntry(transferFN(je.getName()));
                    out.putNextEntry(jarAdd);
                    if (je.getName().endsWith(".java")) {
                        out.write(transferSource(jsource).getBytes(charset));
                    } else {
                        out.write(jsource.getBytes(charset));
                    }
                }
                out.close();
                stream.close();
            } finally {
                jarsour.close();
            }
        } catch (IOException e) {
            System.out.println("Process src jar failed: " + e.getMessage());
            e.printStackTrace();
            System.exit(-1);
        }
    }

    public static String transferFN(String f) {
        String prefix = UnixArgsutil.ARGS.kvalue("prefix");
        String p = prefix.replace('.', '/');
        return p + (p.endsWith("/") ? "" : "/") + f;
    }

    public static String transferSource(String s) {
        BufferedReader br = new BufferedReader(new StringReader(s));
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        try {
            for (String line; ((line = br.readLine()) != null);) {
                if (line.trim().startsWith("package")) {
                    pw.println(line.trim().replaceFirst("package\\s+(.*)",
                                                        "package " + UnixArgsutil.ARGS.kvalue("prefix") + "\\.$1"));
                } else {
                    pw.println(line);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        pw.flush();
        return sw.toString();
    }
}
