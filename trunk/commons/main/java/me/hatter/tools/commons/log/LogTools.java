package me.hatter.tools.commons.log;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.ServiceLoader;

import me.hatter.tools.commons.log.impl.LogUtilLogTool;
import me.hatter.tools.commons.log.spi.LogToolProvider;

@SuppressWarnings("deprecation")
public class LogTools {

    public static final String                        LOGTOOL_PROVIDER       = "logtool.provider";
    private static final MDCTool                      NULL_MDC_TOOL          = (MDCTool) Proxy.newProxyInstance(LogTools.class.getClassLoader(),
                                                                                                                new Class<?>[] { MDCTool.class },
                                                                                                                new InvocationHandler() {

                                                                                                                    @Override
                                                                                                                    public Object invoke(Object proxy,
                                                                                                                                         Method method,
                                                                                                                                         Object[] args)
                                                                                                                                                       throws Throwable {
                                                                                                                        return null;
                                                                                                                    }
                                                                                                                });
    volatile private static LogToolProvider           defaultLogToolProvider = null;
    private static final Map<String, LogToolProvider> logToolMap             = new HashMap<String, LogToolProvider>();

    static {
        try {
            String logtoolProvider = System.getProperty(LOGTOOL_PROVIDER);
            ServiceLoader<LogToolProvider> logTools = ServiceLoader.load(LogToolProvider.class);
            synchronized (LogTools.class) {
                Iterator<LogToolProvider> logToolProviderIterator = logTools.iterator();
                while (logToolProviderIterator.hasNext()) {
                    LogToolProvider logToolProvider = logToolProviderIterator.next();
                    LogUtil.info("Find log tool provider: " + logtoolProvider.getClass().getName());
                    logToolMap.put(logToolProvider.getClass().getName(), logToolProvider);
                    if (logtoolProvider.getClass().getName().equalsIgnoreCase(logtoolProvider)) {
                        defaultLogToolProvider = logToolProvider;
                    }
                }
            }
            if (defaultLogToolProvider == null) {
                LogUtil.info("Not match log tool provider found, use buildin log util!");
            } else {
                LogUtil.info("Match log tool provider found: " + defaultLogToolProvider.getClass().getName());
            }
        } catch (Exception e) {
            LogUtil.error("Load logtool providers failed!", e);
        }
    }

    public static MDCTool getMDCTool() {
        if (defaultLogToolProvider == null) {
            return NULL_MDC_TOOL;
        } else {
            return defaultLogToolProvider.provideMDC();
        }
    }

    public static LogTool getLogTool(Class<?> clazz) {
        return getLogTool(clazz.getName());
    }

    public static LogTool getLogTool(String name) {
        if (defaultLogToolProvider == null) {
            return new LogUtilLogTool(); // buildin
        } else {
            return defaultLogToolProvider.provide(name);
        }
    }
}
