package me.hatter.tools.markdown4j;

import java.io.IOException;

import org.markdown4j.Markdown4jProcessor;

public class DivTest {

    public static void main(String[] args) throws IOException {
        String md3 = "hello world\n=======\n%%%% div style=\"float:right;\"\n" //
                     + "print \"hi\";\n" //
                     + "\n%%%%\n" //
                     + "\n\n\n"//
                     + "%%%% div\n" //
                     + "[!](http://hatter.in/)\n\n" //
                     + "\n%%%%\n"//
                     + "abc";
        System.out.println(new Markdown4jProcessor().process(md3));
    }
}
