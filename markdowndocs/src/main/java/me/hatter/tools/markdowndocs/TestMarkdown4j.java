package me.hatter.tools.markdowndocs;

import java.io.IOException;

import org.markdown4j.Markdown4jProcessor;

public class TestMarkdown4j {

    public static void main(String[] args) throws IOException {
        System.out.println(new Markdown4jProcessor().process("aaaaa\n-----\nhelloworld\nhello"));
    }
}
