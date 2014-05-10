package me.hatter.tools.java8.string;

import java.io.BufferedReader;
import java.io.StringReader;

/**
 * Created by hatterjiang on 5/10/14.
 */
public class Test2 {
    public static void main(String[] args) {
        BufferedReader br = new BufferedReader(new StringReader("hello\nworld"));
        br.lines().map(String::toUpperCase).forEach(System.out::println);
    }
}
