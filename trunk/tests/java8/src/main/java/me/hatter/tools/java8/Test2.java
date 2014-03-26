package me.hatter.tools.java8;

import me.hatter.tools.commons.collection.CollectionUtil;

import java.util.Arrays;
import java.util.List;

/**
 * Created by hatterjiang on 3/26/14.
 */
public class Test2 {
    public static void main(String[] args) {
        List<String> list = Arrays.asList("abc", "cbc", "bbc");
        List<String> list2 = CollectionUtil.filter(list, (v) -> v.startsWith("a"));
        System.out.println(list2);
    }
}
