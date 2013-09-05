package me.hatter.tools.jsspserver.util;

import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import me.hatter.tools.commons.reflect.ReflectUtil;

public class ResponseUtil {

    private static ConcurrentMap<Class<?>, Method> responseGetStatusMap     = new ConcurrentHashMap<Class<?>, Method>();
    private static ConcurrentMap<Class<?>, Object> responseNullGetStatusMap = new ConcurrentHashMap<Class<?>, Object>();

    public static HttpServletResponse getRealResponse(HttpServletResponse httpServletResponse) {
        if (httpServletResponse == null) {
            return httpServletResponse;
        }
        int loopLimit = 0;
        while (httpServletResponse instanceof HttpServletResponseWrapper) {
            if (loopLimit++ > 10) {
                throw new IllegalStateException("Un-wrap http servlet response loop exceed!");
            }
            httpServletResponse = (HttpServletResponse) ((HttpServletResponseWrapper) httpServletResponse).getResponse();
        }
        return httpServletResponse;
    }

    public static int getIntResponseStatus(HttpServletResponse httpServletResponse) {
        Integer status = getResponseStatus(httpServletResponse);
        if (status == null) {
            throw new IllegalStateException("Get response status failed: " + httpServletResponse.getClass());
        }
        return status.intValue();
    }

    public static Integer getResponseStatus(HttpServletResponse httpServletResponse) {
        try {
            httpServletResponse = getRealResponse(httpServletResponse);
            Class<?> clazz = httpServletResponse.getClass();
            Method getStatusMethod = responseGetStatusMap.get(clazz);
            if (getStatusMethod != null) {
                return (Integer) getStatusMethod.invoke(httpServletResponse, new Object[0]);
            }
            if (responseNullGetStatusMap.containsKey(clazz)) {
                return null;
            }
            getStatusMethod = ReflectUtil.getDeclaredMethod(clazz, "getStatus", new Class<?>[0]);
            if (getStatusMethod == null) {
                responseNullGetStatusMap.putIfAbsent(clazz, new Object());
                return null;
            } else {
                if (!getStatusMethod.isAccessible()) {
                    getStatusMethod.setAccessible(true);
                }
                responseGetStatusMap.putIfAbsent(clazz, getStatusMethod);
                return (Integer) getStatusMethod.invoke(httpServletResponse, new Object[0]);
            }
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }
}
