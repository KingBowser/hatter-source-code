package me.hatter.tools.java8.bae64;

import java.util.Base64;

/**
 * Created by hatterjiang on 4/19/14.
 */
public class Test1 {

    public static void main(String[] args) {
        String s = "";
        for (int i = 0; i < 2000; i++) {
            s += ((char) (i));
        }
        System.out.println(Base64.getEncoder().encodeToString(s.getBytes()));
        System.out.println();
        System.out.println();
        System.out.println(Base64.getUrlEncoder().encodeToString(s.getBytes()));
        System.out.println();
        System.out.println();
        System.out.println(Base64.getMimeEncoder().encodeToString(s.getBytes()));
    }
}
