package me.hatter.tools.commons.password;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

public class PasswordCheckTool {

    private long                              checkIpMillis     = TimeUnit.MINUTES.toMillis(1);
    private long                              checkUserMillis   = TimeUnit.MINUTES.toMillis(1);
    private long                              maxIpErrorCount   = 10;
    private long                              maxUserErrorCount = 3;
    private Map<String, List<Long>>           ipErrorLogMap     = new ConcurrentHashMap<String, List<Long>>();
    private ConcurrentMap<String, List<Long>> userErrorLogMap   = new ConcurrentHashMap<String, List<Long>>();

    public boolean canLogin(String ip, String user, boolean isSimpleCheckPass) {
        long now = System.currentTimeMillis();
        if (!checkCountAndTime(ipErrorLogMap, ip, isSimpleCheckPass, now, maxIpErrorCount, checkIpMillis)) {
            return false;
        }
        if (!checkCountAndTime(userErrorLogMap, ip, isSimpleCheckPass, now, maxUserErrorCount, checkUserMillis)) {
            return false;
        }

        if (!isSimpleCheckPass) {
            // any thing?
            return false;
        } else {
            synchronized (ipErrorLogMap) {
                ipErrorLogMap.remove(ip);
            }
            synchronized (userErrorLogMap) {
                userErrorLogMap.remove(ip);
            }
        }
        return true;
    }

    private boolean checkCountAndTime(Map<String, List<Long>> map, String key, boolean isSimpleCheckPass, long now,
                                      long checkCount, long checkMillis) {
        synchronized (map) {
            List<Long> ipErrorLogList = map.get(key);
            if (ipErrorLogList != null && ipErrorLogList.size() >= checkCount) {
                if ((Math.abs(now - ipErrorLogList.get(0))) < checkMillis) {
                    return false;
                }
            }
            if (!isSimpleCheckPass) {
                if (ipErrorLogList.size() >= checkCount) {
                    ipErrorLogList.remove(0);
                }
                ipErrorLogList.add(now);
            }
        }
        return true;
    }
}
