package me.hatter.tools.commons.log;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ServiceLoader;

import me.hatter.tools.commons.log.annotation.AutoInit;
import me.hatter.tools.commons.log.impl.LogUtilLogTool;
import me.hatter.tools.commons.log.spi.LogToolProvider;
import me.hatter.tools.commons.string.StringUtil;

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
            String logtoolProviderProperty = System.getProperty(LOGTOOL_PROVIDER);
            ServiceLoader<LogToolProvider> logTools = ServiceLoader.load(LogToolProvider.class);
            List<LogToolProvider> autoInitLogToolsProviderList = new ArrayList<LogToolProvider>();

            synchronized (LogTools.class) {
                Iterator<LogToolProvider> logToolProviderIterator = logTools.iterator();
                while (logToolProviderIterator.hasNext()) {
                    LogToolProvider logToolProvider = logToolProviderIterator.next();
                    boolean autoInit = logToolProvider.getClass().isAnnotationPresent(AutoInit.class);

                    LogUtil.info("Find log tool provider: " + logToolProvider.getClass().getName() + ", auto init: "
                                 + autoInit);
                    logToolMap.put(logToolProvider.getClass().getName(), logToolProvider);
                    if (logToolProvider.getClass().getName().equalsIgnoreCase(logtoolProviderProperty)) {
                        defaultLogToolProvider = logToolProvider;
                    }
                    if (autoInit) {
                        autoInitLogToolsProviderList.add(logToolProvider);
                    }
                }
            }
            FIND_DLTP_ROUND: if ((logtoolProviderProperty != null) && (defaultLogToolProvider == null)
                                 && (!logToolMap.isEmpty())) {
                // second round match
                for (Entry<String, LogToolProvider> logToolEntry : logToolMap.entrySet()) {
                    String shortClassName = StringUtil.substringAfterLast(logToolEntry.getKey(), ".");
                    if (shortClassName.equalsIgnoreCase(logtoolProviderProperty)) {
                        defaultLogToolProvider = logToolEntry.getValue();
                        break FIND_DLTP_ROUND;
                    }
                }
                // third round match
                for (Entry<String, LogToolProvider> logToolEntry : logToolMap.entrySet()) {
                    String shortClassName = StringUtil.substringAfterLast(logToolEntry.getKey(), ".");
                    if (shortClassName.toLowerCase().startsWith(logtoolProviderProperty.toLowerCase())) {
                        defaultLogToolProvider = logToolEntry.getValue();
                        break FIND_DLTP_ROUND;
                    }
                }
            }
            if ((defaultLogToolProvider == null) && (!autoInitLogToolsProviderList.isEmpty())) {
                if (autoInitLogToolsProviderList.size() > 1) {
                    LogUtil.warn("More than one auto init log tool providers: " + autoInitLogToolsProviderList.size());
                }
                defaultLogToolProvider = autoInitLogToolsProviderList.get(0);
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
