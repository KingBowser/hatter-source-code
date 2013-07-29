package me.hatter.tests.jdktest;

import sun.net.InetAddressCachePolicy;

public class InetAddressCachePolicyTest {

    public static void main(String[] args) {
        System.out.println(InetAddressCachePolicy.get());
        System.out.println(InetAddressCachePolicy.getNegative());
        System.out.println(System.getSecurityManager());
    }
}
