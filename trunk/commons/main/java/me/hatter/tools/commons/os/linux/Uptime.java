package me.hatter.tools.commons.os.linux;

public class Uptime {

    private double up;
    private double idle;

    public Uptime(double up, double idle) {
        this.up = up;
        this.idle = idle;
    }

    public double getUp() {
        return up;
    }

    public void setUp(double up) {
        this.up = up;
    }

    public double getIdle() {
        return idle;
    }

    public void setIdle(double idle) {
        this.idle = idle;
    }

    @Override
    public String toString() {
        return "Uptime [up=" + up + ", idle=" + idle + "]";
    }
}
