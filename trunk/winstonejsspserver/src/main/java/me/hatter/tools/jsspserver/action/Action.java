package me.hatter.tools.jsspserver.action;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.hatter.tools.commons.log.LogTool;
import me.hatter.tools.commons.log.LogTools;

public interface Action {

    public static class DoAction {

        private static final LogTool logTool = LogTools.getLogTool(DoAction.class);

        HttpServletRequest           request;
        HttpServletResponse          response;
        Map<String, Object>          context;

        public DoAction(HttpServletRequest request, HttpServletResponse response, Map<String, Object> context) {
            this.request = request;
            this.response = response;
            this.context = context;
        }

        public void doAction(String clazz) {
            try {
                Class<?> jsspActionClazz = Class.forName(clazz);
                if (logTool.isInfoEnable()) {
                    logTool.info("Found jssp action: " + jsspActionClazz);
                }
                if (Action.class.isAssignableFrom(jsspActionClazz)) {
                    Action a = ((Action) jsspActionClazz.newInstance());
                    Map<String, Object> c = a.doAction(request, response);
                    if (c != null) {
                        context.putAll(c);
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static final String JSSP_ACTION = "jsspaction";
    public static final String JSSP_TYPE   = "jssptype";

    Map<String, Object> doAction(HttpServletRequest request, HttpServletResponse response);
}
