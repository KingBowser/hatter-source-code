package me.hatter.tools.test;

import java.util.Arrays;

public class SplitTest {

    public static void main(String[] args) {
        String s1 = "a\rb\rc";
        String s2 = "a\nb\nc";
        String s3 = "a\r\nb\r\nc";

        System.out.println(Arrays.asList(s1.split("\r\n")));
        System.out.println(Arrays.asList(s1.split("\n\r")));
        System.out.println(Arrays.asList(s2.split("\r\n")));
        System.out.println(Arrays.asList(s2.split("\n\r")));
        System.out.println(Arrays.asList(s3.split("\r\n")));
        System.out.println(Arrays.asList(s3.split("\n\r")));
    }
}
