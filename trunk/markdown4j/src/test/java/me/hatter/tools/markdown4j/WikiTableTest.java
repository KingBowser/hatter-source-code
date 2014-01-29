package me.hatter.tools.markdown4j;

import java.io.IOException;

import org.markdown4j.Markdown4jProcessor;

public class WikiTableTest {

    public static void main(String[] args) throws IOException {
        String md = "hello world\n=======\n%%%% wikiTable a=b,c=d\n" //
                    + "||*2 ___th1 || th2 \\\n ~~hello~~ ||\n" //
                    + "| td1 |*2  td3 |\n" //
                    + "\n%%%%\n";
        System.out.println(new Markdown4jProcessor().process(md));
    }
}
