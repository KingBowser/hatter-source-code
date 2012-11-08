package me.hatter.jprofiler.object;

import java.util.List;

import me.hatter.jprofiler.util.ListUtil;

public class JStackTrace {

    private List<String> stackTraceList;

    public int matchsCount(JStackTrace jStackTrace) {
        int count = 0;
        List<String> stl1 = ListUtil.notNull(ListUtil.reverse(stackTraceList));
        List<String> stl2 = ListUtil.notNull(ListUtil.reverse(jStackTrace.stackTraceList));
        for (int i = 0; ((i < stl1.size()) && (i < stl2.size())); i++) {
            String st1 = stl1.get(i);
            String st2 = stl2.get(i);
            if (st1.equals(st2)) {
                count++;
            }
        }
        return count;
    }

    public boolean containsComAlibaba() {
        if (stackTraceList == null) {
            return false;
        }
        for (String trace : stackTraceList) {
            if (trace.contains("com.alibaba.")) {
                return true;
            }
        }
        return false;
    }

    public List<String> getStackTraceList() {
        return stackTraceList;
    }

    public void setStackTraceList(List<String> stackTraceList) {
        this.stackTraceList = stackTraceList;
    }
}
