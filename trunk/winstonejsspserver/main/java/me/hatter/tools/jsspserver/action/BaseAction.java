package me.hatter.tools.jsspserver.action;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class BaseAction implements Action {

    @Override
    public Map<String, Object> doAction(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> context = new HashMap<String, Object>();
        doAction(request, response, context);
        return context;
    }

    abstract protected void doAction(HttpServletRequest request, HttpServletResponse response,
                                     Map<String, Object> context);
}
