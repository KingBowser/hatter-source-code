package me.hatter.jprofiler.object;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.Thread.State;

public class JStack {

    private String      name;
    private boolean     isDeamon;
    private String      priority;
    private String      tid;
    private String      nid;
    private String      others;
    private State       state;
    private JStackTrace stackTrace;

    public String dumpJStack() {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        pw.println("\"" + name + "\"" + (isDeamon ? " deamon" : "") + " prio=" + priority + " tid=" + tid + " nid="
                   + nid + " " + others);
        pw.println("    " + State.class.getName().replace('$', '.') + ": " + state);
        if ((stackTrace != null) && (stackTrace.getStackTraceList() != null)) {
            for (String ln : stackTrace.getStackTraceList()) {
                pw.println("        " + ln);
            }
        }
        return sw.toString().trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isDeamon() {
        return isDeamon;
    }

    public void setDeamon(boolean isDeamon) {
        this.isDeamon = isDeamon;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public String getNid() {
        return nid;
    }

    public void setNid(String nid) {
        this.nid = nid;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public JStackTrace getStackTrace() {
        return stackTrace;
    }

    public void setStackTrace(JStackTrace stackTrace) {
        this.stackTrace = stackTrace;
    }

    public String getOthers() {
        return others;
    }

    public void setOthers(String others) {
        this.others = others;
    }
}
