package me.hatter.tests.localdnscache;

import java.lang.reflect.Field;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

import me.hatter.tools.commons.exception.ExceptionUtil;
import me.hatter.tools.commons.log.LogUtil;
import me.hatter.tools.commons.reflect.ReflectUtil;
import me.hatter.tools.commons.string.StringUtil;

import sun.net.spi.nameservice.NameService;

public class LocalDnsCacheNameService implements NameService {

    /**
     * 目前写死的更新策略是每N（10）分钟钟更新一次缓存数据，也可以增加更多方式，如最后使用的10个域名更新，有统一的DNS缓存memcached更新数据等，也可以增加端口监听，高效刷新解析记录
     */
    private static final ConcurrentMap<String, InetAddress[]> lookupAllHostAddrCacheMap = new ConcurrentHashMap<String, InetAddress[]>();

    public static interface InetAddressImplCaller {

        InetAddress[] lookupAllHostAddr(String hostname) throws UnknownHostException;

        String getHostByAddr(byte[] addr) throws UnknownHostException;
    }

    private static InetAddressImplCaller implCaller;
    static {
        try {
            Field implFiled = InetAddress.class.getDeclaredField("impl");
            implFiled.setAccessible(true);
            implCaller = ReflectUtil.cast(implFiled.get(null), InetAddressImplCaller.class);
        } catch (Exception e) {
            throw ExceptionUtil.wrapRuntimeException(e);
        }
    }

    private static final Thread          updateThread = new Thread() {

                                                          public void run() {
                                                              while (true) {
                                                                  try {
                                                                      Thread.sleep(TimeUnit.MINUTES.toMillis(10));
                                                                  } catch (InterruptedException e1) {
                                                                  }
                                                                  // 比较死板的更新方式，更多方式参看“lookupAllHostAddrCacheMap”的注释
                                                                  for (String hostname : lookupAllHostAddrCacheMap.keySet()) {
                                                                      LogUtil.info("Update lookupAllHostAddrCacheMap: "
                                                                                   + hostname);
                                                                      try {
                                                                          InetAddress[] inetAddress = implCaller.lookupAllHostAddr(hostname);
                                                                          lookupAllHostAddrCacheMap.put(hostname,
                                                                                                        inetAddress);
                                                                      } catch (Exception e) {
                                                                          LogUtil.error("Error in update lookupAllHostAddrCacheMap: "
                                                                                        + hostname);
                                                                      }
                                                                  }
                                                              }
                                                          }
                                                      };
    static {
        updateThread.setDaemon(true);
        updateThread.start();
    }

    public String getHostByAddr(byte[] addr) throws UnknownHostException {
        List<String> ips = new ArrayList<String>();
        for (byte b : addr) {
            ips.add(String.valueOf(((int) b) & 0xFF));
        }
        LogUtil.info("Call getHostByAddr: " + StringUtil.join(ips, "."));
        return implCaller.getHostByAddr(addr);
    }

    public InetAddress[] lookupAllHostAddr(String hostname) throws UnknownHostException {
        InetAddress[] inetAddress = lookupAllHostAddrCacheMap.get(hostname);
        if (inetAddress != null) {
            LogUtil.info("Call lookupAllHostAddr/C: " + hostname);
            return inetAddress.clone();
        }
        LogUtil.info("Call lookupAllHostAddr/N: " + hostname);
        inetAddress = implCaller.lookupAllHostAddr(hostname);
        lookupAllHostAddrCacheMap.put(hostname, inetAddress.clone());
        return inetAddress;
    }
}
