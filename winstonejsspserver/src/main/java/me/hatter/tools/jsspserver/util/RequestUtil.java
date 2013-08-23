package me.hatter.tools.jsspserver.util;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletRequest;

import me.hatter.tools.commons.converter.ConverterUtil;
import me.hatter.tools.commons.exception.ExceptionUtil;
import me.hatter.tools.commons.reflect.ReflectUtil;
import me.hatter.tools.commons.string.StringUtil;

public class RequestUtil {

    public static interface ValueGetter {

        String getValue(String key);

        List<String> getValues(String key);
    }

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
            return Arrays.asList(request.getParameterValues(makeKey(key)));
        }

        private String makeKey(String key) {
            if (StringUtil.isBlank(prefix)) {
                return key;
            }
            return prefix + "." + key;
        }
    }

    public static <T> T parse(ValueGetter getter, Class<T> clazz) {
        try {
            T obj = clazz.newInstance();
            List<Field> fields = ReflectUtil.getDeclaredFields(clazz);
            for (Field field : fields) {
                if (!field.isAccessible()) {
                    field.setAccessible(true);
                }
                String fn = StringUtil.toUnder(field.getName());
                if (ConverterUtil.isClassMultiple(field.getClass())) {
                    field.set(obj, ConverterUtil.convertToFit(getter.getValues(fn), field));
                } else {
                    field.set(obj, ConverterUtil.convertToFit(getter.getValue(fn), field));
                }
            }
            return obj;
        } catch (Exception e) {
            throw ExceptionUtil.wrapRuntimeException(e);
        }
    }
}
