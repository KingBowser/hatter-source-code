package me.hatter.tools.jsspserver.util;

import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletRequest;

import me.hatter.tools.commons.reflect.ReflectUtil;
import me.hatter.tools.commons.reflect.ReflectUtil.ValueGetter;
import me.hatter.tools.commons.string.StringUtil;

public class RequestUtil {

    public static class RequestValueGetter implements ValueGetter {

        private ServletRequest request;
        private String         prefix;

        public RequestValueGetter(ServletRequest request) {
            this(request, null);
        }

        public RequestValueGetter(ServletRequest request, String prefix) {
            this.request = request;
            this.prefix = prefix;
        }

        public String getValue(String key) {
            return request.getParameter(makeKey(key));
        }

        public List<String> getValues(String key) {
            String[] values = request.getParameterValues(makeKey(key));
            return (values == null) ? null : Arrays.asList(values);
        }

        private String makeKey(String key) {
            if (StringUtil.isBlank(prefix)) {
                return key;
            }
            return prefix + "." + key;
        }
    }

    public static <T> T parse(ServletRequest request, Class<T> clazz) {
        return parse(new RequestValueGetter(request), clazz);
    }

    public static <T> T parse(ValueGetter getter, Class<T> clazz) {
        return ReflectUtil.parse(getter, clazz);
    }

    public static <T> T fill(ServletRequest request, T obj) {
        return fill(new RequestValueGetter(request), obj);
    }

    public static <T> T fill(ValueGetter getter, T obj) {
        return ReflectUtil.fill(getter, obj);
    }
}
