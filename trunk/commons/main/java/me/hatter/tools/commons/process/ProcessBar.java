package me.hatter.tools.commons.process;

import me.hatter.tools.commons.datetime.DateTimeUtil;
import me.hatter.tools.commons.datetime.Times;
import me.hatter.tools.commons.string.StringUtil;

public class ProcessBar {

    private long startMillis = 0L;
    private int  length      = 80;
    private int  pos         = 0;
    private int  percent     = 0;
    private int  curr        = 0;
    private int  total       = 0;

    public ProcessBar() {
        this.startMillis = System.currentTimeMillis();
    }

    public long getStartMillis() {
        return startMillis;
    }

    public ProcessBar start(long startMillis) {
        this.startMillis = startMillis;
        return this;
    }

    public ProcessBar len(int length) {
        this.length = length;
        return this;
    }

    public ProcessBar update(int curr, int total) {
        if (total < 1) {
            // illegal
            return this;
        }
        this.pos = Math.min(((int) (((double) curr / total) * length)), length);
        this.percent = Math.min(((int) (((double) curr / total) * 100)), 100);
        this.curr = curr;
        this.total = total;
        return this;
    }

    public String display() {
        StringBuilder sb = new StringBuilder();
        long now = System.currentTimeMillis();
        String bar = StringUtil.repeat("+", this.pos) + StringUtil.repeat("-", (length - this.pos));

        sb.append("[").append(bar).append("]");
        sb.append(" ").append(((percent < 10) ? "0" + percent : percent) + "%");

        String cost = DateTimeUtil.format(Times.MILLISECONDS(now - startMillis));
        sb.append(" cost: " + cost);

        sb.append(" left~ ");
        if (curr < 1) {
            sb.append("- s");
        } else {
            long leftMillis = Math.max(((now - startMillis) * ((long) total - curr) / (long) curr), 0L);
            sb.append(DateTimeUtil.format(Times.MILLISECONDS(leftMillis)));
        }

        return sb.toString();
    }
}
