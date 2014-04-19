package me.hatter.tools.java8;

import java.util.Arrays;
import java.util.List;

/**
 * Created by hatterjiang on 4/3/14.
 */
public class Test6 {
    public static void main(String[] args) {
        List<String> l = Arrays.asList("1", "2", "3");
        System.out.println(l.stream().mapToInt((a) -> Integer.parseInt(a)).sum());
    }
}
