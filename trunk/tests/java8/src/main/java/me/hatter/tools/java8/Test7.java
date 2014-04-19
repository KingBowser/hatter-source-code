package me.hatter.tools.java8;

import java.util.stream.Stream;

/**
 * Created by hatterjiang on 4/4/14.
 */
public class Test7 {
    public static void main(String[] args) {
        Stream.of(1, 2, 3).forEach(System.out::println);
    }
}
