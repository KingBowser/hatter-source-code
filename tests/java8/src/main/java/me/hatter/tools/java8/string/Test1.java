package me.hatter.tools.java8.string;

import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;

/**
 * Created by hatterjiang on 4/30/14.
 */
public class Test1 {
    public static void main(String[] args) {
        StringJoiner sj = new StringJoiner(", ", "[", "]");
        sj.add("hello").add("world").add("end");
        System.out.println(sj.toString());

        List<Integer> nums = Arrays.asList(1, 3, 5, 7);
        System.out.println(nums.stream().map(i -> String.valueOf(i)).collect(Collectors.joining(", ")));
    }
}
