package me.hatter.tools.markdown4j;

import java.io.IOException;

import org.markdown4j.Markdown4jProcessor;

public class MTest {

    public static void main(String[] args) throws IOException {
        System.out.println(new Markdown4jProcessor().process("[](http://hatter.me/)"));
        System.out.println(new Markdown4jProcessor().process("[!](http://hatter.me/)"));
        System.out.println(new Markdown4jProcessor().process("[!!](http://hatter.me/)"));
        System.out.println(new Markdown4jProcessor().process("[!aa](http://hatter.me/)"));
        System.out.println(new Markdown4jProcessor().process("[http://hatter.me/]()"));
        System.out.println(new Markdown4jProcessor().process("[aaaa](\"hello world\")"));
        System.out.println(new Markdown4jProcessor().process("hello ~~world~~ ssss"));
    }
}
