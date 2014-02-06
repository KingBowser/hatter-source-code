package me.hatter.tools.markdown4j;

import java.io.IOException;

import org.markdown4j.Markdown4jProcessor;

public class PrettifyTest {

    public static void main(String[] args) throws IOException {
//        String md = "hello world\n=======\n%%%% prettify\n" //
//                    + "print \"hi\";\n" //
//                    + "\n%%%%\n";
//        System.out.println(new Markdown4jProcessor().process(md));
//        System.out.println("=========================================");
//        String md2 = "hello world\n=======\n%%%% prettify ln=1\n" //
//                     + "print \"hi\";\n" //
//                     + "\n%%%%\n";
//        System.out.println(new Markdown4jProcessor().process(md2));
//        System.out.println("=========================================");
        String md3 = "hello world\n=======\n%%%% prettify ln=1\n" //
                     + "print \"hi\";\n" //
                     + "\n%%%%\n" //
                     + "\n\n\n"//
                     + "%%%% prettify\n" //
                     + "print \"hello\";\n" //
                     + "\n%%%%\n"//
                     + "abc";
        System.out.println(new Markdown4jProcessor().process(md3));
    }
}
