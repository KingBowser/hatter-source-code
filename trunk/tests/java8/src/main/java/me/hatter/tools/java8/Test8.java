package me.hatter.tools.java8;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by hatterjiang on 4/4/14.
 */
public class Test8 {
    public static void main(String[] args) {
        List<String> l = new ArrayList<>(Arrays.asList("d", "a", "c", "e", "b", "y", "z", "x"));
        l.sort((a, b) -> a.compareTo(b));
        l.forEach((a) -> System.out.print(a + " "));
    }
}
