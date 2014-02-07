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
        System.out.println(new Markdown4jProcessor().process("* aaa\n" //
                                                             + "    * 1\n"//
                                                             + "    * 2\n"//
                                                             + "    * 3\n"//
                                                             + "* bbb\n"//
                                                             + "    1. 11\n"//
                                                             + "    1. 22\n"//
                                                             + "    1. 33\n"//
                                                             + ""));
    }
}
