package me.hatter.tests.diff;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import difflib.DiffUtils;
import difflib.Patch;

public class DiffTest {

    public static void main(String[] args) {

        List<String> original = new ArrayList<String>(Arrays.asList("aaa", "xbbb", "ccc", "ddd"));
        List<String> revised = new ArrayList<String>(Arrays.asList("aaa", "xxx", "bbb", "ccc"));
        Patch p = DiffUtils.diff(original, revised);
        System.out.println(p.getDeltas());
    }
}
