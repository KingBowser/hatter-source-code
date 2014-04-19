package me.hatter.tools.java8;

import java.util.Arrays;

/**
 * Created by hatterjiang on 4/3/14.
 */
public class Test5 {
    public static void main(String[] args) {
        Arrays.asList(1, 2, 3).forEach((a) -> System.out.println(a));
        Arrays.asList(1, 2, 3).forEach(System.out::println);
    }
}
