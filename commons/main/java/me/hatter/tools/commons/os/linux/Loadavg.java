package me.hatter.tools.commons.os.linux;

public class Loadavg {

    private double avg1;
    private double avg5;
    private double avg15;

    public Loadavg(double avg1, double avg5, double avg15) {
        this.avg1 = avg1;
        this.avg5 = avg5;
        this.avg15 = avg15;
    }

    public double getAvg1() {
        return avg1;
    }

    public void setAvg1(double avg1) {
        this.avg1 = avg1;
    }

    public double getAvg5() {
        return avg5;
    }

    public void setAvg5(double avg5) {
        this.avg5 = avg5;
    }

    public double getAvg15() {
        return avg15;
    }

    public void setAvg15(double avg15) {
        this.avg15 = avg15;
    }

    @Override
    public String toString() {
        return "Loadavg [avg1=" + avg1 + ", avg5=" + avg5 + ", avg15=" + avg15 + "]";
    }
}
