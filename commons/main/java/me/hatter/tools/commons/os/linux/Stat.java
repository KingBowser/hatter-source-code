package me.hatter.tools.commons.os.linux;

import java.util.List;

public class Stat {

    public static class CPU {

        private long use;
        private long nice;
        private long sys;
        private long idle;
        private long iow;
    }

    public static class Page {

        private long in;
        private long out;
    }

    public static class Swap {

        private long in;
        private long out;
    }

    private static class Process {

        private long intr;
        private long ctxt;
        private long btime;
        private long count;
        private long running;
        private long blocked;
    }

    private CPU       cpu;
    private List<CPU> cpus;
    private Page      page;
    private Swap      swap;
    private Process   process;
}
