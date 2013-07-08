package me.hatter.tools.unix4j;

import org.unix4j.Unix4j;

public class Unix4JTest {

    public static void main(String[] args) {
        Unix4j.ls().xargs().cat();
    }
}
