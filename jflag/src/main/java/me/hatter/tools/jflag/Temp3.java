package me.hatter.tools.jflag;

import java.util.List;

import sun.management.FlagAgent;

public class Temp3 {

    public static void main(String[] args) {
        List<FlagAgent> flagList = FlagAgent.getAllFlags();
        for (FlagAgent flag : flagList) {
            System.out.println(flag.getVMOption());
        }
    }
}
