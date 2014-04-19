package me.hatter.tools.java8;

import java.util.Arrays;

/**
 * Created by hatterjiang on 4/4/14.
 */
public class Test9 {
    public static void main(String[] args) {
        System.out.println(Arrays.asList(1, 4, 3, 7, 2).stream().reduce((a, b) -> a + b).get());
//        Arrays.asList(1,2,3).stream().collect
//        String a = () -> "hello world";
    }
}
