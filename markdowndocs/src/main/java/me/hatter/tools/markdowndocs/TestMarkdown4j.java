package me.hatter.tools.markdowndocs;

import java.io.IOException;

import org.markdown4j.Markdown4jProcessor;

/**
 * https://code.google.com/p/markdown4j/ <br>
 * https://github.com/rjeschke/txtmark
 * 
 * @author hatterjiang
 */
public class TestMarkdown4j {

    public static void main(String[] args) throws IOException {
        System.out.println(new Markdown4jProcessor().process("[](http://hatter.me/)"));
        System.out.println(new Markdown4jProcessor().process("[http://hatter.me/]()"));
        System.out.println(new Markdown4jProcessor().process("[aaaa](\"hello world\")"));
        System.out.println(new Markdown4jProcessor().process("hello ~~world~~ ssss"));
    }
}
