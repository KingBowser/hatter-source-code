package me.hatter.tools.java8;

import java.util.Arrays;
import java.util.List;

/**
 * Created by hatterjiang on 3/26/14.
 */
public class Test1 {
    public static void main(String[] args) {
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8);
        list.stream().filter((v) -> v % 2 == 0).forEach(System.out::println);
        System.out.println("------------------------------------------------");
        list.parallelStream().filter((v) -> v % 2 == 0).forEach(System.out::println);
    }
}
